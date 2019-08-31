package com.leihui.dao;

import com.leihui.domain.User;

import java.util.List;

/**
 * 用户持久层接口
 * @author leihui
 */
public interface UserDao {
    /**
     * 查询所有用户
     * @return
     */
    List<User> findAll();

    /**
     * 根据ID查询用户
     * @param id
     * @return
     */
    User findById(Integer id);

}
