package edu.hhu.dao;

import edu.hhu.domain.Member;

/**
 * 会员的持久层代码
 */
public interface IMemberDao {
    //根据电话号码查询客户信息
    Member findMemberByTelephone(String telephone);

    //插入用户信息
    void addMember(Member member);

     //查询到该月为止累计的会员数量
    int findMemberTotalCountByMonth(String date);

    //获取今天新增的会员数
    Integer findTodayNewMember(String today);

    //获取总会员数
    Integer findTotalMember();

    //获取本周新增会员数
    Integer findThisWeekNewMember(String thisWeekMonday);

    //获取本月新增会员数
    Integer findThisMonthNewMember(String firstDayOfMonth);
}
