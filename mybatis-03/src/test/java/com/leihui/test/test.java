package com.leihui.test;

import com.leihui.dao.AccountDao;
import com.leihui.dao.RoleDao;
import com.leihui.dao.UserDao;
import com.leihui.domain.Account;
import com.leihui.domain.AccountUser;
import com.leihui.domain.Role;
import com.leihui.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resources;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试mybatis的CRUD操作
 * @author leihui
 */
public class test {

    private InputStream in = null;
    private SqlSessionFactory factory = null;
    private SqlSession session = null;
    private AccountDao accountDao = null;
    private RoleDao roleDao = null;
    private UserDao userDao = null;

    /**
     * 测试前
     */
    @Before
    public void init(){
        //1.读取配置文件，生成字节输入流
        in = Resources.class.getResourceAsStream("/SqlMapConfig.xml");
        //System.out.println(inputStream);
        //2.获取SqlSessionFactory
        factory = new SqlSessionFactoryBuilder().build(in);
        //3.获取SqlSession对象
        session = factory.openSession();
        //4.获取dao的代理对象
        userDao = session.getMapper(UserDao.class);
        roleDao = session.getMapper(RoleDao.class);
        accountDao = session.getMapper(AccountDao.class);

    }

    /**
     * 测试后 提交数据 释放资源
     * @throws IOException
     */
    @After
    public void destroy() throws IOException {
        //提交数据
        session.commit();
        //6.释放资源
        session.close();
        in.close();
    }

    /**
     * 查询所有
     */
    @Test
    public void test01(){
        List<AccountUser> accounts = accountDao.findAll();
        for (AccountUser account : accounts){
            System.out.println(account);
        }
    }
    @Test
    public void test02(){
        List<Role> roles = roleDao.findAll();
        for (Role role : roles){
            System.out.println("------------");
            System.out.println(role);
            System.out.println(role.getUsers());
        }
    }

    @Test
    public void test03(){
        List<User> users = userDao.findAll();
        for (User user : users){
            System.out.println("------------");
            System.out.println(user);
            System.out.println(user.getRoles());
        }
    }

}

