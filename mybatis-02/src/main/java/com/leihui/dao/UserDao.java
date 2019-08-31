package com.leihui.dao;

import com.leihui.domain.QueryVo;
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

    /**
     * 根据名称模糊查询
     * @param username
     * @return
     */
    List<User> findByName(String username);


    List<User> findUserByVo(QueryVo vo);

    /**
     * 根据传入参数条件查询
     * @param user  条件：有可能是用户名，有可能是性别，有可能是地址，都有可能
     * @return
     */
    List<User> findUserByCondition(User user);

    /**
     * 根据QueryVo中的ID集合，查询用户信息
     * @param vo
     * @return
     */
    List<User> findUserInIds(QueryVo vo);
}
