package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.service.ReportService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service( interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() {
        Map<String,Object> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //1获取当前日期
            String today = DateUtils.parseDate2String(new Date());
            //2根据日期查询今日新增会员数
            Integer todayNewMember = memberDao.getMemberByToday(today);
            //3查询会员总数
            Integer totalMember = memberDao.getAllMember();
            //4.1获取本周一对应的日期
            String thisWeekMonday = sdf.format(DateUtils.getThisWeekMonday());
            //4.2查询本周新增会员
            Integer thisWeekNewMember = memberDao.getMemberByThisWeek(thisWeekMonday);
            //5查询本月新增会员
            String todayMonthFrist = sdf.format(DateUtils.getFirstDay4ThisMonth());
            Integer thisMonthNewMember = memberDao.getMemberByTodayMonthFrist(todayMonthFrist);
            //6今日预约数
            Integer todayOrderNumber = orderDao.getOrderByToday(today);
            //7今日出游数
            Integer todayVisitsNumber = orderDao.todayVisitsNumber(today);
            //8本周预约
            Integer thisWeekOrderNumber = orderDao.thisWeekOrderNumber(thisWeekMonday,today);
            //9本周出游
            Integer thisWeekVisitsNumber = orderDao.thisWeekVisitsNumber(thisWeekMonday,today);
            //10本月预约数
            Integer thisMonthOrderNumber = orderDao.thisMonthOrderNumber(todayMonthFrist);
            //11本月出游数
            Integer thisMonthVisitsNumber = orderDao.thisMonthVisitsNumber(todayMonthFrist,today);
            //套餐
            List<Map> hotSetmeal = orderDao.hotSetmeal();

            map.put("reportDate",today);
            map.put("todayNewMember",todayNewMember);
            map.put("totalMember",totalMember);
            map.put("thisWeekNewMember",thisWeekNewMember);
            map.put("thisMonthNewMember",thisMonthNewMember);
            map.put("todayOrderNumber",todayOrderNumber);
            map.put("todayVisitsNumber",todayVisitsNumber);
            map.put("thisWeekOrderNumber",thisWeekOrderNumber);
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
            map.put("thisMonthOrderNumber",thisMonthOrderNumber);
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
            map.put("hotSetmeal",hotSetmeal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }
}
