

### mybatis学习笔记（三）

#### mybatis多表查询

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

##### 一对一查询

根据数据库表创建两个实体类 User 类和 Account 类，实现查询账户信息的同时，也要查询对应的用户信息。

- 方式一：通过子类查询

定义 AccountCustomer 类中要包含账户信息同时还要包含用户信息，所要在定义 AccountUser 类时可以继承 Account 类。 

```java
public class AccountUser extends Account {
	private String username;
	private String address;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
    public String getAddress() {
    	return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    @Override
    public String toString() {
    	return super.toString() + " AccountUser [username=" + username + ",
   		address=" + address + "]";
    }
}
```

注意：AccountUser . toString () 调用了父类的 toSting () ,那么在调用 AccountUser . toString () 时，也会执行 Account . toString () ，就会得到所有想要的信息。

定义账户持久层dao接口

```java
public interface AccountDao {
    /**
    * 查询所有账户，同时获取账户的所属用户名称以及它的地址信息
    * @return
    */
    List<AccountUser> findAll();
}
```

定义xml查询配置文件

```xml
<mapper namespace="com.leihui.dao.AccountDao">
	<!-- 配置查询所有操作-->
	<select id="findAll" resultType="accountuser">
		select a.*,u.username,u.address from account a,user u where a.uid =u.id;
	</select>
</mapper>
```

- 方式二：建立实体类的关系

使用 resultMap，定义专门的 resultMap 用于映射一对一查询结果。通过面向对象的(has a)关系可以得知，在 Account 类中加入一个 User 类的对象来代表这个账户是哪个用户的。

定义账户持久层dao接口

```java
public interface AccountDao {
    /**
    * 查询所有账户，同时获取账户的所属用户名称以及它的地址信息
    * @return
    */
    List<Account> findAll();
}
```

定义xml查询配置文件

```xml
<!-- 建立对应关系 -->
<resultMap type="account" id="accountMap">
    <id column="aid" property="id"/>
    <result column="uid" property="uid"/>
    <result column="money" property="money"/>
    <!-- 它是用于指定从表的引用实体属性的 -->
    <association property="user" javaType="user">
        <id column="id" property="id"/>
        <result column="username" property="username"/>
        <result column="sex" property="sex"/>
        <result column="birthday" property="birthday"/>
        <result column="address" property="address"/>
    </association>
</resultMap>
<select id="findAll" resultMap="accountMap">
    select u.*,a.id as aid,a.uid,a.money from account a,user u where a.uid =u.id;
</select>

```

##### 一对多查询

用户信息和它的账户信息为一对多关系，并且查询过程中如果用户没有账户信息，此时也要将用户信息查询出来，左外连接查询比较合适。



```xml
<resultMap type="user" id="userMap">
    <id column="id" property="id"/>
    <result column="username" property="username"/>
    <result column="address" property="address"/>
    <result column="sex" property="sex"/>
    <result column="birthday" property="birthday"/>
    <!-- collection 是用于建立一对多中集合属性的对应关系ofType 用于指定集合元素的数据类型-->
    <collection property="accounts" ofType="account">
        <id column="aid" property="id"/>
        <result column="uid" property="uid"/>
        <result column="money" property="money"/>
    </collection>
</resultMap>
<!-- 配置查询所有操作 -->
<select id="findAll" resultMap="userMap">
	select u.*,a.id as aid ,a.uid,a.money from user u left outer join account a on u.id = a.uid
</select>
```

#####  多对多查询

角色表

|   Field   |       Type       | Comment  |
| :-------: | :--------------: | :------: |
|    ID     | int(11) NOT NULL |   编号   |
| ROLE_NAME | varchar(30) NULL | 角色名称 |
| ROLE_DESC | varchar(60) NULL | 角色描述 |

用户角色中间表

| Field |       Type       | Comment  |
| :---: | :--------------: | :------: |
|  UID  | int(11) NOT NULL | 用户编号 |
|  RID  | int(11) NOT NULL | 角色编号 |

根据表创建实体类 User 类和 Role 类，在类中各自添加对方的对象引用。

在 User 类中添加 private List<Role> roles ；在 Role 类中添加 private List<User> users 。

RoleDao的xml文件

```xml
<!--定义role表的ResultMap-->
<resultMap id="roleMap" type="role">
    <id property="roleId" column="rid"/>
    <result property="roleName" column="role_name"/>
    <result property="roleDesc" column="role_desc"/>
    <collection property="users" ofType="user">
        <id column="id" property="id"/>
        <result property="username" column="username"/>
        <result property="birthday" column="birthday"/>
        <result property="address" column="address"/>
        <result property="sex" column="sex"/>
    </collection>
</resultMap>

<!--查询所有-->
<select id="findAll" resultMap="roleMap">
    SELECT u.* ,r.id AS rid,r.role_name,r.role_desc FROM role r
    LEFT OUTER JOIN user_role ur ON r.id = ur.rid
    LEFT OUTER JOIN USER u ON u.id = ur.uid
</select>
```

UserDao的xml文件

```xml
<!--定义role表的ResultMap-->
<resultMap id="userMap" type="user">
    <id column="id" property="id"/>
    <result property="username" column="username"/>
    <result property="birthday" column="birthday"/>
    <result property="address" column="address"/>
    <result property="sex" column="sex"/>
    <collection property="roles" ofType="role">
        <id property="roleId" column="rid"/>
        <result property="roleName" column="role_name"/>
        <result property="roleDesc" column="role_desc"/>
    </collection>
</resultMap>

<!--查询所有-->
<select id="findAll" resultMap="userMap">
    SELECT u.* ,r.id as rid,r.role_name,r.role_desc FROM user u
    LEFT OUTER JOIN user_role ur ON u.id = ur.uid
    LEFT OUTER JOIN role r ON r.id = ur.rid
</select>
```

