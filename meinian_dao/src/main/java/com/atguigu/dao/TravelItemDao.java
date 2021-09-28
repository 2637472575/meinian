package com.atguigu.dao;

import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TravelItemDao {
    void add(TravelItem travelItem);

    Page paging(String queryString);

    void delete(Integer id);

    void update(TravelItem travelItem);

    List<TravelItem> getAll();
}
