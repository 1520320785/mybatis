package com.leihui.test;

import com.leihui.dao.UserDao;
import com.leihui.domain.QueryVo;
import com.leihui.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 测试mybatis的CRUD操作
 * @author leihui
 */
public class test {

    private InputStream in = null;
    private SqlSessionFactory factory = null;
    private SqlSession session = null;
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
     * @throws Exception
     */
    @Test
    public void test01() throws Exception {
        //1.读取配置文件，生成字节输入流
        InputStream inputStream = Resources.class.getResourceAsStream("/SqlMapConfig.xml");
        //System.out.println(inputStream);
        //2.获取SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //3.获取SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //4.获取dao的代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //5.执行查询所有的方法
        List<User> users = userDao.findAll();
        for (User user : users){
            System.out.println(user);
        }
        //6.释放资源
        sqlSession.close();
        inputStream.close();
    }



    /**
     * 查询一个
     */
    @Test
    public void test05(){

        User user=userDao.findById(45);
        System.out.println(user);
    }

    /**
     * 根据名称模糊查询
     */
    @Test
    public void test06(){

        List<User> users=userDao.findByName("%up%");
        for (User user : users) {
            System.out.println(user);
        }
    }



    /**
     * 根据名称查询对象，参数变成一个QueryVo对象
     */
    @Test
    public void test08(){
        QueryVo vo = new QueryVo();
        User u = new User();
        u.setUsername("%up%");
        vo.setUser(u);
        List<User> users=userDao.findUserByVo(vo);
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 查询所有 if和where标签
     */
    @Test
    public void test09(){
        User u = new User();
        u.setUsername("save");
        u.setSex("女");
        List<User> users = userDao.findUserByCondition(u);
        for (User user : users){
            System.out.println(user);
        }
    }

    /**
     * 查询所有 foreach标签
     */
    @Test
    public void test10(){
        QueryVo vo = new QueryVo();
        List<Integer> list = new ArrayList<Integer>();
        list.add(41);
        list.add(42);
        list.add(43);
        list.add(52);
        vo.setIds(list);
        List<User> users = userDao.findUserInIds(vo);
        for (User user : users){
            System.out.println(user);
        }
    }
}

