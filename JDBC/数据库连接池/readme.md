### 数据库连接池——实现连接的重复利用，提高数据库运行访问的效率；对后端数据库的限流保护的作用。
目前最广泛使用的DBCP连接池为例，有三个jar包：

commons-dbcp.jar,commons-pool.jar,commons-logging.jar.
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
