package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import com.atguigu.util.POIUtils;
import org.apache.xmlbeans.impl.jam.mutable.MElement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {
    @Reference
    OrderSettingService orderSettingService;

    /**
     * 实现文件上传将文件数据读到数据库
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try{
            List<String[]> strings = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettingList = new ArrayList<>();
            for (String[] stringslist : strings) {
                OrderSetting orderSetting = new OrderSetting();
                String orderDate = stringslist[0];
                String number = stringslist[1];
                orderSetting.setOrderDate(new Date(orderDate));
                orderSetting.setNumber(Integer.parseInt(number));
                orderSettingList.add(orderSetting);
            }
            orderSettingService.add(orderSettingList);
            return new Result(true,MessageConstant.UPLOAD_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

    @RequestMapping("/getNumByDate")
    public Result getNumByDate(String date){
        try{
            List<Map> list = orderSettingService.getNumByDate(date);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/addOrUpdateNum")
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    public Result addOrUpdateNum(String chooseDate, @RequestBody Map value){
        try{
            String message = orderSettingService.addOrUpdateNum(chooseDate, Integer.parseInt(value.get("value").toString()));
            return new Result(true,message);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ORDERSETTING_FAIL);
        }
    }
}
