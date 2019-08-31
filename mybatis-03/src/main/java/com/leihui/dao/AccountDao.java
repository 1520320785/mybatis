package com.leihui.dao;

import com.leihui.domain.Account;
import com.leihui.domain.AccountUser;

import java.util.List;

/**
 * 账户持久层
 * @author leihui
 */
public interface AccountDao {
    /**
     * 查询所有账户
     * @return
     */
    List<AccountUser> findAll();

}
