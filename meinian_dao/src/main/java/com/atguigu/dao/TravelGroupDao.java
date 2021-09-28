package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {
    void add(TravelGroup travelGroup);

    void addTravelItemIdsAndtravelGroupId(Map<String, Integer> map);

    Page paging(String queryString);

    void update(TravelGroup travelGroup);

    List<Integer> getTravelItemByGroupId(Integer traveGroupId);

    void delete(Integer groupId);

    void deleteTraveGroup(Integer groupId);

    List<TravelGroup> findAll();
}
