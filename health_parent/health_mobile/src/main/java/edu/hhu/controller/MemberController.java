package edu.hhu.controller;

import com.alibaba.fastjson.JSON;
import edu.hhu.constant.MessageConstant;
import edu.hhu.constant.RedisMessageConstant;
import edu.hhu.domain.Member;
import edu.hhu.entity.Result;
import edu.hhu.service.IMemberService;
import org.apache.dubbo.config.annotation.Reference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 会员的表示层
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private IMemberService memberService;
    /**
     * 用户登录
     */
    @RequestMapping("/login.do")
    public Result Login(@RequestBody Map map, HttpServletResponse response){
        try {
            //1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
            String m_validateCode = (String) map.get("validateCode");
            String telephone = (String)map.get("telephone");
            String r_validateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            if (m_validateCode != null && r_validateCode != null && m_validateCode.equals(r_validateCode)){
                //2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
                Member member = memberService.findMemberByTelephone(telephone);
                if (member == null){
                    member = new Member();
                    member.setPhoneNumber(telephone);
                    member.setRegTime(new Date());
                    memberService.addMember(member);
                }
                //3、向客户端写入Cookie，内容为用户手机号
                Cookie cookie = new Cookie("login_member_telephone",telephone);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(cookie);
                //4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
                String json = JSON.toJSON(member).toString();
                jedisPool.getResource().setex(telephone,60 * 30,json);
                return new Result(true,MessageConstant.LOGIN_SUCCESS);
            }else{
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SYSTEM_ERROR);
        }

    }
}
