package edu.hhu.controller;

import edu.hhu.constant.MessageConstant;
import edu.hhu.constant.RedisMessageConstant;
import edu.hhu.entity.Result;
import edu.hhu.service.IOrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 订单的表示层
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private IOrderService orderService;

    /**
     * 提交订单
     */
    @RequestMapping("/submitOrder.do")
    public Result submitOrder( @RequestBody  Map map) { //提交的信息涉及多个实体类的信息, 故采用map集合进行封装
        //1.判断验证码是否正确 通过将前台提交的验证码与redis缓存中的进行对比如果一致则验证码通过
        String o_validateCode = (String) map.get("validateCode");//表单提交的验证码
        String telephone = (String) map.get("telephone");
        String r_validateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        System.out.println(o_validateCode);
        System.out.println(r_validateCode);
        if (o_validateCode != null && r_validateCode != null && o_validateCode.equals(r_validateCode)){
            return orderService.processOrder(map);
        }
        return new Result(false, MessageConstant.VALIDATECODE_ERROR);
    }

    /**
     * 当预约成功后，跳转到订单成功界面 返回相应数据
     */

    @RequestMapping("/findOrderById.do")
    public Result findOrderById(Integer id){
        try {
            Map map = orderService.findOrderById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SYSTEM_ERROR);
        }

    }


}
