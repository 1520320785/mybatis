### mybatis学习笔记（二）

#### mybatis的连接池

mybatis将他自己的数据源分为三类：

- UNPOOLED： 不使用连接池的数据源
- POOLED：使用链接池的数据源
- JNDI：使用 JNDI 实现的数据源

mybatis内部分别实现了 java.sql.DataSource 接口的 UnpooledDataSource，PooledDataSource 类来表示 UNPOOLED,POOLED 类型的数据源。PooledDataSource 持有一个 UnpooledDataSource 的引用，当PooledDataSource 需要创建 java.sql.Connection 实例对象时，还是通过 UnpooledDataSource 来创建。PooledDataSource 只是提供一种缓存连接池机制。



mybatis 是通过工厂模式来创建数据源 DataSource 对象的，mybatis 定义了抽象工厂接口org.apache.ibatis.datasource.DataSourceFactory ,通过其 getDataSource () 方法返回数据源 DataSource 。




```flow
st=>start:开始
io=>inputoutput:PooledConnection conn = null
cond1=>condition:idleConnections.size() > 0
sub1=>subroutine:conn = idleconnections.remove(0)
cond2=>condition:activeConnections.size() < MAX_ACTIVE
op=>operation:conn = new PooledConnection(dataSource.getConnection(),this)
sub3=>subroutine:oldestActiveConnection = activeConnections.get(0)
cond3=>condition:是否失效
op2=>operation:activeConnections.remove(oldestActiveConnection)
op1=>operation:activeConnection.add(conn)
oi=>inputoutput:reture conn
op3=>operation:conn = new PooledConnection(oldestActiveConnection.getRealConnection(),this)oldestActiveConnection.invalidate()
e=>end:结束

st->io->cond1
cond1(yes,)->sub1(left)->oi->e
cond1(no)->cond2
cond2(yes)->op->op1->oi->e
cond2(no)->sub3(right)->cond3
cond3(yes)->op2->op3->oi->e
cond3(no)->cond2
```

```flow
st=>start:ks
e=>end:js
st->e
```

```flow
st=>start:ks
e=>end:js
st->e
```

```mermaid
graph LR
A[明确活动目标] -->B[提出活动创意]
B --> C[产出活动方案]
```



最后发现，真正连接打开的时间点，只是在执行 SQL 语句时，才会进行。其实这样做也可以进一步发现，数据

库连接是最为宝贵的资源，只有在要用到的时候，才去获取并打开连接，当用完了就再立即将数据库连接归还到连接池中。 

#### mybatis动态sql

##### if标签

多条件查询

```xml
<select id="findUser" resultType="user" parameterType="user">
	select * from user where 1=1
	<if test="username!=null and username != '' ">
		and username = #{username}
	</if>
	<if test="sex != null">
		and sex = #{sex}
	</if>
</select>
```

注意： &lt;if&gt;标签的 test 属性中写的是对象的属性名，如果是包装类的对象要使用 OGNL 表达式的写法。
另外要注意 where 1=1 的作用~！

##### where标签

简化 where 1=1

```xml
<select id="findUser" resultType="user" parameterType="user">
	select * from user 
	<where>
        <if test="username!=null and username != '' ">
        	and username = #{username}
        </if>
        <if test="sex != null">
    		and sex = #{sex}
    	</if>
    </where>
</select>
```

##### foreach标签

范围查询

SELECT * FROM USERS WHERE username = 'zhangsan' AND id IN (10,14,16) 

```xml
<!-- 查询所有用户在 id 的集合之中 -->
<select id="findIds" resultType="user" parameterType="queryvo">
	select * from user  
	<where>
		<if test="ids != null and ids.size() > 0">
			<foreach collection="ids" open="id in ( " close=")" item="uid"
			separator=",">
				#{uid}
			</foreach>
        </if>
	</where>
</select>
```

注意：SQL 语句：select 字段 from user where id in (?)
&lt; foreach &gt; 标签用于遍历集合，它的属性：

- collection : 代表要遍历的集合元素，注意编写时不要写 # {}
- open : 代表语句的开始部分
- close : 代表结束部分

##### sql标签

Sql 标签可将重复的 sql 提取出来，使用时用 include 标签引用即可，最终达到 sql 重用的目的。 

```xml
<sql id="defaultSql">
	select * from user
</sql>
<!-- 查询所有用户在 id 的集合之中 -->
<select id="findIds" resultType="user" parameterType="queryvo">
	<!-- select * from user -->
    <include refid="defaultSql"></include>
	<where>
		<if test="ids != null and ids.size() > 0">
			<foreach collection="ids" open="id in ( " close=")" item="uid"
			separator=",">
				#{uid}
			</foreach>
        </if>
	</where>
</select>
```

