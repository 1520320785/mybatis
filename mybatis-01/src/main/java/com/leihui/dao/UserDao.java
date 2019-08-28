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
     * 保存用户
     * @param user
     */
    void saveUser(User user);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 删除用户
     * @param id
     */
    void deleteUser(Integer id);

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

    /**
     * 查询总行数
     * @return
     */
    int findTotal();

    List<User> findUserByVo(QueryVo vo);
}
