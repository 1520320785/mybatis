import com.leihui.dao.AccountDao;
import com.leihui.dao.UserDao;
import com.leihui.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class test {
    private InputStream in = null;
    private SqlSessionFactory factory = null;
    private SqlSession session = null;
    private AccountDao accountDao = null;
    private UserDao userDao = null;

    @Before
    public void init(){
        //1.读取配置文件
        in = Resources.class.getResourceAsStream("/SqlMapConfig.xml");
        factory = new SqlSessionFactoryBuilder().build(in);
        session = factory.openSession();
        userDao = session.getMapper(UserDao.class);
    }

    @After
    public void destroy() throws IOException {
        session.commit();
        session.close();
        in.close();
    }

    @Test
    public void test01(){
        User user = new User();
        user.setUsername("zhangsan");
        user.setSex("男");
        user.setAddress("bj");
        user.setBirthday(new Date());
        userDao.saveUser(user);
    }
    @Test
    public void test02(){
        User user = new User();
        user.setId(50);
        user.setUsername("zhangsan");
        user.setSex("男");
        user.setAddress("bj");
        user.setBirthday(null);
        userDao.updateUser(user);
    }
}
