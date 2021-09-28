package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    SetmealDao setmealDao;
    @Autowired
    JedisPool jedisPool;
    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {
        setmealDao.add(setmeal);
        //获取对应套餐游的id
        Integer setmealId = setmeal.getId();
        //向中间表存储数据
        addSetmealTravelGroup(travelgroupIds,setmealId);

        //将文件存储到redis用于解决垃圾图片
        String img = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
    }

    @Override
    public PageResult paging(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page page = setmealDao.paging(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> getSetmeal() {
        List<Setmeal> list = setmealDao.getSetmeal();
        return list;
    }

    @Override
    public Setmeal finfById(Integer id) {
            Map<String,Object> map = new HashMap<>();
            Setmeal setmeal = setmealDao.findById(id);
        return setmeal;
    }

    private void addSetmealTravelGroup(Integer[] travelgroupIds,Integer setmealId){
        for (Integer travelgroupId : travelgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmealId",setmealId);
            map.put("travelgroupId",travelgroupId);
            setmealDao.addSetmealTravelGroup(map);
        }
    }
}
