package com.leihui.dao;

import com.leihui.domain.Role;

import java.util.List;

/**
 * Role持久层dao接口
 * @author leihui
 */
public interface RoleDao {
    /**
     * 查询所有
     * @return
     */
    List<Role> findAll();
}
