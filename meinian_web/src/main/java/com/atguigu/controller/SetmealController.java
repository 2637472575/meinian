package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    SetmealService setmealService;
    @Autowired
    JedisPool jedisPool;

    /**
     * 上传图片
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        try{
            //获取文件名，并生成唯一名字
            String name = imgFile.getOriginalFilename();
            int i = name.lastIndexOf(".");
            //生成后缀
            String suffix = name.substring(i);
            //生成文件名前缀
            String prefix = UUID.randomUUID().toString().replace("-", "");
            //拼串
            String fileName = prefix + suffix;
            //储存到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //调用工具类上传到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        }catch (Exception e){
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加套餐游
     * @param travelgroupIds
     * @param setmeal
     * @return
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    public Result add(Integer[] travelgroupIds, @RequestBody Setmeal setmeal){
        try{
            setmealService.add(travelgroupIds,setmeal);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/paging")
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    public PageResult paging(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.paging
                (queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }
}
