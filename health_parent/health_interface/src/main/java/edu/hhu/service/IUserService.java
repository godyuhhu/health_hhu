package edu.hhu.service;

import edu.hhu.domain.User;

/**
 * 用户登录的接口
 */
public interface IUserService {
    //根据用户名查询出用户信息 将用户所具有的角色和权限均要查询出来
    User findUserByName(String username);
}
