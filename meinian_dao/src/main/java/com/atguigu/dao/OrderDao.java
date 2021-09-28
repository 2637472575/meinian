package com.atguigu.dao;

import com.atguigu.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findOrder(Order order);

    void addOrder(Order order);

    Map findById(Integer id);

    List<Map> getSetMealCount();

    Integer getOrderByToday(String today);

    Integer todayVisitsNumber(String today);

    Integer thisWeekOrderNumber(@Param("thisWeekMonday") String thisWeekMonday,@Param("today") String today);

    Integer thisWeekVisitsNumber(@Param("thisWeekMonday")String thisWeekMonday,@Param("today") String today);

    Integer thisMonthOrderNumber(String todayMonthFrist);

    Integer thisMonthVisitsNumber(@Param("todayMonthFrist") String todayMonthFrist, @Param("today") String today);

    List<Map> hotSetmeal();
}
