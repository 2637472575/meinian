package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service( interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {
    @Autowired
    TravelGroupDao travelGroupDao;

    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.add(travelGroup);
        Integer travelGroupId = travelGroup.getId();
        addTravelItemIdsAndtravelGroupId(travelItemIds,travelGroupId);
    }

    @Override
    public PageResult paging(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page page = travelGroupDao.paging(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void update(Integer[] travelItemIds,TravelGroup travelGroup) {
        //获取跟团游id
        Integer groupId = travelGroup.getId();
        //根据跟团游id删除所对应的自由行
        travelGroupDao.delete(groupId);
        //根据跟团游id添加自由行
        addTravelItemIdsAndtravelGroupId(travelItemIds,groupId);
        travelGroupDao.update(travelGroup);
    }

    @Override
    public List<Integer> getTravelItemByGroupId(Integer traveGroupId) {
        List<Integer> list = travelGroupDao.getTravelItemByGroupId(traveGroupId);
        list.forEach(System.out::println);
        return list;
    }

    @Override
    public void delete(Integer groupId) {
        travelGroupDao.delete(groupId);
        travelGroupDao.deleteTraveGroup(groupId);
    }

    @Override
    public List<TravelGroup> finfAll() {
        List<TravelGroup> list = travelGroupDao.findAll();
        return list;
    }

    private  void addTravelItemIdsAndtravelGroupId(Integer[] travelItemIds,Integer travelGroupId){
        for (Integer travelItemId : travelItemIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("travelGroupId",travelGroupId);
            map.put("travelItemId",travelItemId);
            travelGroupDao.addTravelItemIdsAndtravelGroupId(map);
        }
    }
}
