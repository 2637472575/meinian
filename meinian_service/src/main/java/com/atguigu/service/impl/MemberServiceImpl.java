package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;


    @Override
    public Member getMemberByTel(String telephone) {
        Member member = memberDao.getMemberByTelephone(telephone);
        return member;
    }

    @Override
    public void addMember(Member member1) {
        memberDao.addMember(member1);
    }

    @Override
    public List<Integer> getMemberByMonth(List<String> monthList) {
        List<Integer> memberList = new ArrayList<>();

        if(monthList != null){
            for (String month : monthList) {
                //查询这个月之前注册的所有会员
                Integer memberCount = memberDao.getMemberByMonth(month + "-31");
                memberList.add(memberCount);
            }
        }
        return memberList;
    }


}
