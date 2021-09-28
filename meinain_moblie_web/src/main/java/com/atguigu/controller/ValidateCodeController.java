package com.atguigu.controller;

import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.util.SMSUtils;
import com.atguigu.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/sendSMS")
    public Result sendSMS(String telephone){
       //生成四位数验证码
        String validateCode = ValidateCodeUtils.generateValidateCode(4).toString();
        //向指定的手机号发送验证码
        try {
            SMSUtils.sendShortMessage(telephone,validateCode);
            //将验证码保存到redis数据库中
            String CodeKey = "Code"+ telephone;
            jedisPool.getResource().setex(CodeKey,600,validateCode);
            //将用户手机号保存到redis中
            String telephoneKey = "telephone" + telephone;
            jedisPool.getResource().setex(telephoneKey,600,telephone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true, MessageConstant.SEND_SMS_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login( String telephone){
        //发送四位数验证码
        String validateCode = ValidateCodeUtils.generateValidateCode(4).toString();
        try{
            SMSUtils.sendShortMessage(telephone,validateCode);
            String loginKey = "login" + telephone;
            jedisPool.getResource().setex(loginKey,600,validateCode);
            return new Result(true,MessageConstant.SEND_SMS_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,null);
        }
    }
}
