package com.atguigu.service;

import com.atguigu.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member getMemberByTel(String telephone);

    void addMember(Member member1);

    List<Integer> getMemberByMonth(List<String> dateList);


}
