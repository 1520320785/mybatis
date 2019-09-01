package com.leihui.dao;

import com.leihui.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * 用户持久层接口
 * @author leihui
 */
public interface UserDao {

    /**
     * 查询所有
     * @return
     */
    @Select("select * from user")
    @Results(id = "userMap",value = {
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "username",property = "username"),
            @Result(column = "address",property = "address"),
            @Result(column = "sex",property = "sex"),
            @Result(column = "birthday",property = "birthday"),
            @Result(column = "id",property = "account",
                    many = @Many(select = "com.leihui.dao.AccountDao.findByUid",
                            fetchType = FetchType.LAZY))
    })
    List<User> findAll();

    /**
     * 保存用户
     * @param user
     */
    @Insert("insert into user(username,address,sex,birthday)values(#{username},#{address},#{sex},#{birthday})")
    void saveUser(User user);

    /**
     * 根据ID查询用户
     * @param
     * @return
     */
    @Select("select * from user where id = #{uid}")
    User findById(Integer id);
    @Update("update user set username=#{username},sex=#{sex},address=#{address},birthday=#{birthday} where id=#{id}")
    void updateUser(User user);

}
