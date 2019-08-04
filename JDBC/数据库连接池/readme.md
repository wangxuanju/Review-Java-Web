### 数据库连接池——实现连接的重复利用，提高数据库运行访问的效率；对后端数据库的限流保护的作用。
目前最广泛使用的DBCP连接池为例，有三个jar包：

commons-dbcp.jar,commons-pool.jar,commons-logging.jar.
### 什么是连接池？
本质上是一组Java的架包，介于Java应用程序和JDBC数据库物理链接之间，负责帮助应用程序来管理JDBC连接；通过连接池暴露的接口业务程序可以获取数据库连接，使用完毕以后，可以将数据库连接归还到连接池，供下一个线程使用。连接池对JDBC实行有效的管理，在连接池中JDBC连接不足时会自动的创建连接。在空闲的连接比较多时，自动的销毁连接。有多个线程获得数据库连接时，连接池提供排队等待的功能，保证有序的获得数据库连
#### 创建连接池
DBCP使用一个叫BasicDataSource的对象来表示一个连接池。

BasicDataSource:

--setInitalSize();第一次访问数据库连接池很慢，所以需要设置这个连接数，预期业务平均访问量

--setMaxTotal();最大连接数，起到限流保护的作用。

--setsetMaxWaitMills();设置最大等待时间，超出时间报异常。

--setMaxIdle();设置最大等待时间，超过这个值，自动销毁这个连接。

--setMinIdle();空闲连接低于此值，连接池自动触发，创建数据库连接。

#### DBCP的定期检查
BasicDataSource

--setTestWhileIdle(True)

--setMinEvictableIdleTimeMillis()

--setTimeBetweenEvictionRunsMillis();


#### 释放数据库连接
