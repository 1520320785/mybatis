package com.leihui.dao;

import com.leihui.domain.Account;


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
    List<Account> findAll();

    /**
     * 根据用户 id 查询账户信息
     * @param uid
     * @return
     */
    List<Account> findByUid(Integer uid);

}
