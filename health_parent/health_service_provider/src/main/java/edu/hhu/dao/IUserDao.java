package edu.hhu.dao;

import edu.hhu.domain.User;

public interface IUserDao {
    User findUserByName(String username);
}
