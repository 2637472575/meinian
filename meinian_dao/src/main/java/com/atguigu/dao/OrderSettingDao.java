package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    int getByDate(Date orderDate);

    void updateByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<OrderSetting> getNumByDate(Map<String, String> dateMap);

    OrderSetting getOrderSettingByDate(Date date);

    void updateReservations(OrderSetting orderSetting);
}
