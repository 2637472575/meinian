package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.apache.xmlbeans.impl.jam.mutable.MElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service( interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> orderSettingList) {
        for (OrderSetting orderSetting : orderSettingList) {
           int i = orderSettingDao.getByDate(orderSetting.getOrderDate());
           if(i > 0){
               //说明有相同日期的数据，进行修改操作
               orderSettingDao.updateByOrderDate(orderSetting);
           }else {
               orderSettingDao.add(orderSetting);
           }
        }
    }

    @Override
    public List<Map> getNumByDate(String date) {
        //确定日期取值范围
        String beginDate = date + "-1";
        String endDate = date + "-31";
        Map<String,String> dateMap = new HashMap<>();
        dateMap.put("beginDate",beginDate);
        dateMap.put("endDate",endDate);
        //调用方法
        List<OrderSetting> orderSettingList = orderSettingDao.getNumByDate(dateMap);
        //用于储存返回的数据
        List<Map> mapList = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            Map<String,Object> map = new HashMap();
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public String addOrUpdateNum(String chooseDate,Integer value) {
        OrderSetting orderSetting = new OrderSetting();
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        try {
            orderSetting.setOrderDate(sdf.parse(chooseDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        orderSetting.setNumber(value);
        int i = orderSettingDao.getByDate(orderSetting.getOrderDate());
        if(i > 0){
            //修改
            orderSettingDao.updateByOrderDate(orderSetting);
            return MessageConstant.EDIT_ORDERSETTING_SUCCESS;
        }else {
            //添加
            orderSettingDao.add(orderSetting);
            return MessageConstant.ADD_ORDERSETTING_SUCCESS;
        }
    }
}
