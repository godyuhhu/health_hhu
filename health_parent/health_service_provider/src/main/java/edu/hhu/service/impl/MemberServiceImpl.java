package edu.hhu.service.impl;

import edu.hhu.dao.IMemberDao;
import edu.hhu.domain.Member;
import edu.hhu.service.IMemberService;
import edu.hhu.utils.MD5Utils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MemberServiceImpl implements IMemberService {
    @Autowired
    private IMemberDao memberDao;
    //根据电话号码查询该会员是否存在
    @Override
    public Member findMemberByTelephone(String telephone) {
        return memberDao.findMemberByTelephone(telephone);
    }

    //新增会员
    @Override
    public void addMember(Member member) {
        if (member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.addMember(member);
    }

    //查询到该月为止累计的会员数量
    @Override
    public int findMemberTotalCountByMonth(String date) {
        //对 date日期进行处理
        date = date + "-31"; //进行范围查询时进行扩大处理

        return  memberDao.findMemberTotalCountByMonth(date);
    }
}
