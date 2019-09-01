package com.leihui.dao;

import com.leihui.domain.Account;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;


import java.util.List;

/**
 * 账户持久层
 * @author leihui
 */
public interface AccountDao {
    /**
     * 查询所有账户,同时获取账户的所属用户名称以及它的地址信息
     * @return
     */
    @Select("select * from account")
    @Results(id = "resultMap",value = {
            @Result(id = true ,column = "id", property = "id"),
            @Result(column = "uid",property = "uid"),
            @Result(column = "money",property = "money"),
            @Result(column = "uid",property = "user",one = @One(select="com.leihui.dao.UserDao.findById",fetchType= FetchType.EAGER))
    })
    List<Account> findAll();

    /**
     * 根据用户 id 查询账户信息
     * @param uid
     * @return
     */
    @Select("select * from account where uid = #{userId}")
    List<Account> findByUid(Integer uid);

}
