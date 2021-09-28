package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelGroup")
public class TravelGroupController {
    @Reference
    TravelGroupService travelGroupService;

    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('TRAVELGROUP_EDIT')")
    public Result update(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup){
        try{
            travelGroupService.update(travelItemIds,travelGroup);
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
    }


    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('TRAVELGROUP_ADD')")
    public Result add(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup){
        try{
            travelGroupService.add(travelItemIds,travelGroup);
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/paging")
    @PreAuthorize("hasAuthority('TRAVELGROUP_QUERY')")
    public PageResult paging(@RequestBody QueryPageBean queryPageBean){
        System.out.println(queryPageBean);
            PageResult pageResult = travelGroupService.paging
                    (queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return pageResult;
    }


    @RequestMapping("/getTravelItemByGroupId")
    @PreAuthorize("hasAuthority('TRAVELGROUP_QUERY')")
    public Result getTravelItemByGroupId(Integer groupId){
        try{
            List<Integer> list = travelGroupService.getTravelItemByGroupId(groupId);
            return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('TRAVELGROUP_DELETE')")
    public Result delete(Integer groupId){
        try{
            travelGroupService.delete(groupId);
            return new Result(true,MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/fingAll")
    @PreAuthorize("hasAuthority('TRAVELGROUP_QUERY')")
    public Result fingAll(){
        try{
            List<TravelGroup> list = travelGroupService.finfAll();
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }
}
