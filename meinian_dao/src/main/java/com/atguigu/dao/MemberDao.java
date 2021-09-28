package com.atguigu.dao;

import com.atguigu.pojo.Member;

import java.util.List;

public interface MemberDao {
    Member getMemberByTelephone(String telephone);

    void addMember(Member member);

    Integer getMemberByMonth(String month);

    Integer getMemberByToday(String today);

    Integer getAllMember();

    Integer getMemberByThisWeek(String thisWeekMonday);

    Integer getMemberByTodayMonthFrist(String todayMonthFrist);
}
