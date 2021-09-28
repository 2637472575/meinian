package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController//组合注解
@RequestMapping("/travelItem")
public class TravelItemController {
    @Reference
    TravelItemService travelItemService;

    /**
     * 添加自由行
     * @param travelItem
     * @return
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")
    public Result add(@RequestBody TravelItem travelItem){
        try{
            travelItemService.add(travelItem);
            return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
        }catch (Exception e){
           // e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }
    }

    /**
     * 分页查询
     * @param
     * @return
     */
    @RequestMapping("/paging")
    @PreAuthorize("hasAuthority('TRAVELITEM_QUERY')")
    public PageResult paging(@RequestBody QueryPageBean queryPageBean){

        System.out.println(queryPageBean.getCurrentPage() + "  " + queryPageBean.getPageSize());
        PageResult pageResult = travelItemService.paging(
                queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 删除自由行
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE')")
    public Result delete(Integer id){
        try{
            travelItemService.delete(id);
            return new Result(true,MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

    /**
     * 修改自由行
     * @param travelItem
     * @return
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")
    public Result update(@RequestBody TravelItem travelItem){
        try{
            travelItemService.update(travelItem);
            return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }

    /**
     * 获取所有自由行信息
     *
     */
    @RequestMapping("/getAll")
    @PreAuthorize("hasAuthority('TRAVELITEM_QUERY')")
    public Result getAll(){
        try{
            List<TravelItem> list = travelItemService.getAll();
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }
}
