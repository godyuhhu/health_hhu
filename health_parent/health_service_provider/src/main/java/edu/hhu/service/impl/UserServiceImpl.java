package edu.hhu.service.impl;

import edu.hhu.dao.IUserDao;
import edu.hhu.domain.User;
import edu.hhu.service.IUserService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceImpl implements IUserService {
    /**
     * 根据用户名查询用户信息同时查询该用户具有的角色和权限
     */
    @Autowired
    private IUserDao userDao;
    @Override
    public User findUserByName(String username) {
        return userDao.findUserByName(username);
    }
}
