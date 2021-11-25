package edu.hhu.controller;

import edu.hhu.constant.MessageConstant;
import edu.hhu.constant.RedisMessageConstant;
import edu.hhu.entity.Result;
import edu.hhu.utils.SMSUtils;
import edu.hhu.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 发送各类验证码的控制器
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //生成4位数字验证码，以供可以发送短信
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            SMSUtils.sendShortMessage(telephone, code.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将其存入redis中 时间限制为5分钟 过了该时间 该验证码失效
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 60 * 60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //生成4位数字验证码，以供可以发送短信
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        try {
            SMSUtils.sendShortMessage(telephone, code.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SYSTEM_ERROR);
        }
        //将其存入redis中 时间限制为5分钟 过了该时间 该验证码失效
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 60 * 60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
