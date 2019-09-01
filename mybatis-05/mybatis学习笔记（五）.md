### mybatis学习笔记（五）

#### mybatis 注解开发

##### mybatis 的常用注解说明 

|      注解       |                注解说明                |
| :-------------: | :------------------------------------: |
|     @Insert     |                实现新增                |
|     @Update     |                实现更新                |
|     @Delete     |                实现删除                |
|     @Select     |                实现查询                |
|     @Result     |             实现结果集封装             |
|    @Results     | 可以与@Result 一起使用，封装多个结果集 |
|   @ResultMap    |      实现引用@Results 定义的封装       |
|      @One       |          实现一对一结果集封装          |
|      @Many      |          实现一对多结果集封装          |
| @SelectProvider |           实现动态 SQL 映射            |
| @CacheNamespace |         实现注解二级缓存的使用         |

User表：

|  Field   |             Type             | Comment  |
| :------: | :--------------------------: | :------: |
|    id    | int(11) NOT NULL PRIMARY KEY |   主键   |
| username |     varchar(32) NOT NULL     | 用户名称 |
| birthday |        datetime NULL         |   生日   |
|   sex    |         char(1) NULL         |   性别   |
| address  |      varchar(256) NULL       |   地址   |

Account表：

| Field |       Type       |     Comment      |
| :---: | :--------------: | :--------------: |
|  ID   | int(11) NOT NULL |   编号（主键）   |
|  UID  |   int(11) NULL   | 用户编号（外键） |
| MONEY |   double NULL    |       金额       |

##### 使用 Mybatis 注解实现基本 CRUD 

根据表创建实体类 User 类和 Account 类

创建持久层接口

```java
public interface UserDao {
    
    /**
    * 查询所有用户
    * @return
    */
    @Select("select * from user")
    @Results(id="userMap",
    value= {
    @Result(id=true,column="id",property="userId"),
    @Result(column="username",property="userName"),
    @Result(column="sex",property="userSex"),
    @Result(column="address",property="userAddress"),
    @Result(column="birthday",property="userBirthday")
    })
    List<User> findAll();
    
    /**
    * 根据 id 查询一个用户
    * @param userId
    * @return
    */
    @Select("select * from user where id = #{uid} ")
    @ResultMap("userMap")
    User findById(Integer userId);
    
    /**
    * 保存操作
    * @param user
    * @return
    */
    @Insert("insert into
    user(username,sex,birthday,address)values(#{username},#{sex},#{birthday},#{address}
    )")
    @SelectKey(keyColumn="id",keyProperty="id",resultType=Integer.class,before =
    false, statement = { "select last_insert_id()" })
    void saveUser(User user);
            
    /**
    * 更新操作
    * @param user
    * @return
    */
    @Update("update user set
    username=#{username},address=#{address},sex=#{sex},birthday=#{birthday} where id
    =#{id} ")
    void updateUser(User user);
            
    /**
    * 删除用户
    * @param userId
    * @return
    */
    @Delete("delete from user where id = #{uid} ")
    void deleteUser(Integer userId);
            
    /**
    * 查询使用聚合函数
    * @return
    */
    @Select("select count(*) from user ")
    int findTotal();
            
    /**
    * 模糊查询
    * @param name
    * @return
    */
    @Select("select * from user where username like #{username} ")
    List<User> findByName(String name);
}
```

创建SQLMapConfig.xml文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
<!-- 配置 properties 文件的位置 -->
<properties resource="jdbcConfig.properties"></properties>
<!-- 配置别名的注册 -->
<typeAliases>
<package name="com.leihui.domain"/>
</typeAliases>
<!-- 配置环境 -->
<environments default="mysql">
<!-- 配置 mysql 的环境 -->
<environment id="mysql">
<!-- 配置事务的类型是 JDBC -->
<transactionManager type="JDBC"></transactionManager>
<!-- 配置数据源 -->
<dataSource type="POOLED">
<property name="driver" value="${jdbc.driver}"/>
<property name="url" value="${jdbc.url}"/>
<property name="username" value="${jdbc.username}"/>
<property name="password" value="${jdbc.password}"/>
</dataSource>
</environment>
</environments>
<!-- 配置映射信息 -->
<mappers>
<!-- 配置 dao 接口的位置，它有两种方式
第一种：使用 mapper 标签配置 class 属性
第二种：使用 package 标签，直接指定 dao 接口所在的包
-->
<package name="com.leihui.dao"/>
</mappers>
</configuration>
```

##### 使用注解实现复杂关系映射开发 

```xml
@Results 注解
代替的是标签<resultMap>
该注解中可以使用单个@Result 注解，也可以使用@Result 集合
@Results（{@Result（）， @Result（） }）或@Results（@Result（））
@Resutl 注解
代替了 <id>标签和<result>标签
@Result 中 属性介绍：
id 是否是主键字段
column 数据库的列名
property 需要装配的属性名
one 需要使用的@One 注解（@Result（one=@One）（）））
many 需要使用的@Many 注解（@Result（many=@many）（）））
@One 注解（一对一）
代替了<assocation>标签，是多表查询的关键，在注解中用来指定子查询返回单一对象。
@One 注解属性介绍：
select 指定用来多表查询的 sqlmapper
fetchType 会覆盖全局的配置参数 lazyLoadingEnabled。。
使用格式：
@Result(column=" ",property="",one=@One(select=""))
@Many 注解（多对一）
代替了<Collection>标签,是是多表查询的关键，在注解中用来指定子查询返回对象集合。
注意：聚集元素用来处理“一对多”的关系。需要指定映射的 Java 实体类的属性，属性的 javaType
（一般为 ArrayList）但是注解中可以不定义；
使用格式：
@Result(property="",column="",many=@Many(select=""))
```

根据表创建实体类 User 类和 Account 类，在类中添加对方的对象引用

**一对一**

创建持久层接口

AccountDao

```java
@Select("select * from account")
    @Results(id = "resultMap",value = {
            @Result(id = true ,column = "id", property = "id"),
            @Result(column = "uid",property = "uid"),
            @Result(column = "money",property = "money"),
            @Result(column = "uid",property = "user",one = @One(select="com.leihui.dao.UserDao.findById",fetchType= FetchType.EAGER))
    })
List<Account> findAll();

```

UserDao

```java
@Select("select * from user where id = #{uid}")
User findById(Integer id);
```

 **一对多**

创建持久层接口

UserDao

```java
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
```

AccountDao

```java
@Select("select * from account where uid = #{userId}")
List<Account> findByUid(Integer uid);
```

注意：

![1567315652073](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1567315652073.png)

当使用注解时，在resources中持久层对应的目录下不能存在XML文件，否则 mybatis 会报错。