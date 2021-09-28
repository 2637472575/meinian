package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TravelGroupService {
    void add(Integer[] travelItemIds, TravelGroup travelGroup);

    PageResult paging(Integer currentPage, Integer pageSize, String queryString);

    void update(Integer[] travelItemIds,TravelGroup travelGroup);

    List<Integer> getTravelItemByGroupId(Integer traveGroupId);

    void delete(Integer groupId);

    List<TravelGroup> finfAll();
}
