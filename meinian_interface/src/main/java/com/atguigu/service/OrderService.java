package com.atguigu.service;

import com.atguigu.entity.Result;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Result submit(Map map);

    Map findById(Integer id);

    List<Map> getSetMealCount();
}
