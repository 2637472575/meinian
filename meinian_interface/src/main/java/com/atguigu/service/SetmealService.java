package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Integer[] travelgroupIds, Setmeal setmeal);

    PageResult paging(Integer currentPage, Integer pageSize, String queryString);

    List<Setmeal> getSetmeal();

    Setmeal finfById(Integer id);
}
