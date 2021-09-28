package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service( interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderSettingDao orderSettingDao;
    @Autowired
    MemberDao memberDao;

    @Override
    public Result submit(Map map) {
        /*
                    * 业务
            1.判断当前的日期是否可以预约
            2.判断当前的日期预约是否已满
            3.判断是否 是会员
            如果是会员, 避免重复预约
            不是会员, 自动注册成会员，t_member 表插入一条记录
            4.进行预约
            向t_order 表插入一条记录
            向t_ordersetting更新reservations+1
        * */
        //从map中取出所有数据
        //预约日期
        String orderDate = (String) map.get("orderDate");
        //预约者的身份证
        String idCard = (String) map.get("idCard");
        //性别
        String sex = (String) map.get("sex");
        //套餐游对应的id
        Integer setmealId = Integer.parseInt((String)map.get("setmealId"));
        //名字
        String name = (String) map.get("name");
        //电话
        String telephone = (String) map.get("telephone");

        try {
            //1.判断当前的日期是否可以预约
            Date date =DateUtils.parseString2Date(orderDate);
            OrderSetting orderSetting = orderSettingDao.getOrderSettingByDate(date);
            //判断orderSetting为空或者预约人数已满
            if(orderSetting == null){
                return new Result(false, MessageConstant.ORDER_FAIL);
            }else if(orderSetting.getReservations() >= orderSetting.getNumber()){
                return new Result(false,MessageConstant.ORDER_FULL);
            }
            //3.判断是否 是会员根据用户手机号判断是否是会员
            Member member1 = memberDao.getMemberByTelephone(telephone);
            Member member = new Member();
            //不是会员, 自动注册成会员，t_member 表插入一条记录
            if(member1 == null){
                //说明会员不存在,自动为用户进行注册

                member.setPhoneNumber(telephone);
                member.setName(name);
                member.setSex(sex);
                member.setIdCard(idCard);
                member.setRegTime(new Date());
                memberDao.addMember(member);
            }else {
                //如果是会员,避免重复预约
                Order order = new Order();
                Integer memberId =  member.getId();
                order.setMemberId(memberId);
                order.setSetmealId(setmealId);
                order.setOrderDate(date);
                List<Order> orderList = orderDao.findOrder(order);
                if(orderList != null && orderList.size() > 0){
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }
            }
            //4.进行预约
            //向t_order 表插入一条记录
            Member mem = memberDao.getMemberByTelephone(telephone);
            Order order = new Order();
            order.setMemberId(mem.getId());
            order.setSetmealId(setmealId);
            order.setOrderDate(date);
            order.setOrderType("微信预约");
            order.setOrderStatus("未出游");
            orderDao.addOrder(order);
            //向t_ordersetting更新reservations+1
            orderSetting.setReservations(orderSetting.getReservations() + 1);
            orderSettingDao.updateReservations(orderSetting);
            return new Result(true,MessageConstant.ORDER_SUCCESS,order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FAIL);
        }


    }

    @Override
    public Map findById(Integer id) {
       Map orderMap =  orderDao.findById(id);
        return orderMap;
    }

    @Override
    public List<Map> getSetMealCount() {
        List<Map> list = orderDao.getSetMealCount();
        return list;
    }
}
