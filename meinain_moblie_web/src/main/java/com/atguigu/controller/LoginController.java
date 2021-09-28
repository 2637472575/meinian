package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController()
@RequestMapping("/login")
public class LoginController {
    @Reference
    MemberService memberService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map){
        String validateCode =(String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        String loginKey = "login" + telephone;
        String loginCode = jedisPool.getResource().get(loginKey);
        try{
            //如果用户存在则登陆成功,不存在快速注册
            if (validateCode != null&& validateCode.equals(loginCode) ) {
                Member member = memberService.getMemberByTel(telephone);
                if (member == null) {
                    //说明没有该会员，进行快速注册
                    Member member1 = new Member();
                    member1.setPhoneNumber(telephone);
                    member1.setRegTime(new Date());
                    memberService.addMember(member1);
                }
                Cookie cookie = new Cookie("login_member_telephone", telephone);
                cookie.setPath("/");//路径
                cookie.setMaxAge(60 * 60 * 24 * 30);//有效期30天（单位秒）
                response.addCookie(cookie);
                return new Result(true, MessageConstant.LOGIN_SUCCESS);
            }else {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
}
