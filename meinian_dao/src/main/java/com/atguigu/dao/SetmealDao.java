package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void addSetmealTravelGroup(Map<String, Integer> map);

    void add(Setmeal setmeal);

    Page paging(@Param("value") String queryString);

    List<Setmeal> getSetmeal();

    Setmeal findById(Integer id);
}
