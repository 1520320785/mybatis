### mybatis学习笔记（四）

#### mybatis的延迟加载

延迟加载（懒加载）：就是在需要用到数据时才进行加载，不需要用到数据时就不加载数据。

- 好处： 先从单表查询，需要时再从关联表去关联查询，大大提高数据库性能，因为查询单表要比关联查询多张表速度要快。
- 坏处：因为只有当需要用到数据时，才会进行数据库查询，这样在大批量数据查询时，查询工作也要消耗时间，所以可能造成用户等待时间变长，造成用户体验下降。 

account表

| Field |       Type       |   Comment    |
| :---: | :--------------: | :----------: |
|  ID   | int(11) NOT NULL | 编号（主键） |
|  UID  |   int(11) NULL   |   用户编号   |
| MONEY |   double NULL    |     金额     |

user表

|  Field   |         Type         | Comment  |
| :------: | :------------------: | :------: |
|    id    |   int(11) NOT NULL   |   主键   |
| username | varchar(32) NOT NULL | 用户名称 |
| birthday |    datetime NULL     |   生日   |
|   sex    |     char(1) NULL     |   性别   |
| address  |  varchar(256) NULL   |   地址   |

##### 使用 assocation 实现延迟加载 

assocation 适用于一对一和多对一

根据表创建实体类 User 类和 Account 类，在 Account 类中添加 User 类的引用

AccountDao 持久层 xml 文件

```xml
<resultMap type="account" id="accountMap">
    <id column="aid" property="id"/>
    <result column="uid" property="uid"/>
    <result column="money" property="money"/>
    <!-- 它是用于指定从表方的引用实体属性的 -->
    <association property="user" javaType="user"
                 select="com.leihui.dao.UserDao.findById"
                 column="uid">
    </association>
</resultMap>
<select id="findAll" resultMap="accountMap">
    select * from account
</select>

```

UserDao 持久层xml文件

```xml
<select id="findById" resultType="user" parameterType="int" >
    select * from user where id = #{uid}
</select>
```

SQLMapConfig.xml 文件中添加延迟加载设置

```xml
<settings>
    <!--开启延迟加载的全局开关-->
    <setting name="lazyLoadingEnabled" value="true"/>
    <setting name="aggressiveLazyLoading" value="false"/>
</settings>
```

##### 使用 Collection 实现延迟加载 

collection 适用于一对多和多对多

根据表创建实体类 User 类和 Account 类，在 User 类中添加 Account 类的引用

UserDao 持久层xml文件

```xml
<resultMap type="user" id="userMap">
    <id column="id" property="id"/>
    <result column="username" property="username"/>
    <result column="sex" property="sex"/>
    <result column="address" property="address"/>
    <result column="birthday" property="birthday"/>
    <collection property="account" ofType="account" select="com.leihui.dao.AccountDao.findByUid" column="id"/>
</resultMap>

<select id="findAll" resultMap="userMap">
    select * from USER
</select>
```

AccountDao 持久层 xml 文件

```xml
<select id="findByUid" resultType="account">
    select * from account where uid = #{uid}
</select>
```

SQLMapConfig.xml 文件中添加延迟加载设置

```xml
<settings>
    <!--开启延迟加载的全局开关-->
    <setting name="lazyLoadingEnabled" value="true"/>
    <setting name="aggressiveLazyLoading" value="false"/>
</settings>
```

#### mybatis 缓存

Mybatis中的一级缓存和二级缓存

##### 一级缓存

​			它指的是Mybatis中SqlSession对象的缓存。
​			当执行查询之后，查询的结果会同时存入到SqlSession提供一块区域中。该区域的结构是一个Map。			当再次查询同样的数据，mybatis会先去sqlsession中查询是否有，有的话直接拿出来用。
​			当SqlSession对象消失时，mybatis的一级缓存也就消失了。而且SqlSession 去执行 commit 操作（执行插入、更新、删除）和 clearCache 操作，清空 SqlSession 中的一级缓存，这样做的目的为了让缓存中存储的是最新的信息，避免脏读。 

##### 二级缓存

​			它指的是Mybatis中SqlSessionFactory对象的缓存。由同一个SqlSessionFactory对象创建的SqlSession共享其缓存。
​			二级缓存的使用步骤：
​					第一步：让Mybatis框架支持二级缓存（在SqlMapConfig.xml中配置）
​					第二步：让当前的映射文件支持二级缓存（在UserDao.xml中配置）
​					第三步：让当前的操作支持二级缓存（在select标签中配置）

​		注意：二级缓存中存储的是数据，而不是对象。当在使用二级缓存时，所缓存的类一定要实现 java.io.Serializable 接口，这种就可以使用序列化方式来保存对象。



