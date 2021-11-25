package edu.hhu.service;

import edu.hhu.domain.Member;

/**
 * 会员的业务层接口
 */
public interface IMemberService {
    //根据电话号码查询该会员是否存在
    Member findMemberByTelephone(String telephone);
    //新增会员
    void addMember(Member member);

    //查询到该月为止累计的会员数量
    int findMemberTotalCountByMonth(String date);
}
