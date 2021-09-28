package com.atguigu.job;

import com.atguigu.constant.RedisConstant;
import com.atguigu.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    @Autowired
    JedisPool jedisPool;
    /**
     * 定时任务
     * 用于清理图片
     */
    public void clearImg(){
        //取差集
        Set<String> fileNameDiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //删除
        for (String fileName : fileNameDiff) {
            QiniuUtils.deleteFileFromQiniu(fileName);
            System.out.println("垃圾图片[" + fileName + "]已被删除");
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        }
    }
}
