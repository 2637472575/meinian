package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderMobileController {
    @Reference
    OrderService orderService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        //获取用户手机号
        String telephone = (String)map.get("telephone");
        //获取用户输入的验证码
        String validateCode = (String)map.get("validateCode");
        //向redis中查询
        String CodeKey = "Code" + telephone;
        String telephoneKey = "telephone" + telephone;
        String code = jedisPool.getResource().get(CodeKey);
        String tel = jedisPool.getResource().get(telephoneKey);
        //如何用户输入的手机号和验证码都能匹配，则预约成功
        if(telephone.equals(tel) && validateCode.equals(code)){
            Result result = orderService.submit(map);
            return result;
        }else {
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
       Map orderMap = orderService.findById(id);
       return new Result(true,null,orderMap);
    }
}
