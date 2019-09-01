package com.leihui.test;

import com.leihui.dao.AccountDao;
import com.leihui.dao.UserDao;
import com.leihui.domain.User;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class AnnoTest {

    /**
     * 基于注解的 mybatis 使用 main 方法
     * @param args
     */
    public static void main(String[] args) throws IOException {

        //1.读取配置文件，生成字节输入流
        InputStream in = Resources.class.getResourceAsStream("/SqlMapConfig.xml");
        //2.获取SqlSessionFactory
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        //3.获取SqlSession对象
        SqlSession session = factory.openSession();
        //4.获取dao的代理对象
        UserDao userDao = session.getMapper(UserDao.class);
        //5.执行dao的方法
        List<User> users = userDao.findAll();
        for (User user : users){
            System.out.println(user);
            System.out.println(user.getAccount());
        }
        /*User user = new User();
        user.setUsername("zhangsan");
        user.setSex("男");
        user.setAddress("bj");
        user.setBirthday(new Date());
        userDao.saveUser(user);*/
        //6.释放资源
        //session.commit();
        session.close();
        in.close();

    }
}
