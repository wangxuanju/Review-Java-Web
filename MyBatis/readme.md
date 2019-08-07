### 数据库框架——MyBatis
#### ORM
持久化类与数据库表之间的映射关系

对持久化对象的操作自动转换成对关系数据库操作。

### ORM
关系数据库的每一行映射为每一个对象。

关系数据库的每一列映射为对象的每个属性。

### MyBatis
支持自定义的SQL、存储过程和高级映射的持久化框架

使用XML或者注解配置。

能够映射基本数据元素、接口、Java对象到数据库。


### MyBatis的功能架构
接口层、数据处理层、基础支撑层。

### MyBatis工作流机制
根据XML或者注解加载SQL语句、参数映射、结果映射到内存。

应用程序调用API传入参数和SQL ID。

MyBatis自动生成SQL语句完成数据库访问，转换执行结果返回应用程序。

### Mybatis环境搭建
Mybatis-3.2.3.jar;
mysql0connectior-java.5.1.2.jar


### MyBatis优势和劣势
#### 优势：
入门门槛较低

更加灵活。、SQL优化
#### 劣势：
需要自己编写SQL，工作量大

数据库移植性差

### ResultMap
--ResultMap元素是MyBatis中最重要的最强大的元素。

--数据库永远不是你想要的或需要它们是什么样的。

--ResultMap可以实现复杂查询结果到复杂对象关联关系的转化。

### Constructor
#### 类在实例化时，用来注入结果到构造方法中：
--idArg –ID参数；标记结果作为ID可以帮助提高整体功能。

--arg-注入到构造方法中的一个普通结果。

### Collection
#### 实现一对多的关联
--id-一个ID结果，标记结果作为ID可以帮助提高整体功能。

--rssult-注入到字段或JavaBean属性的普通结果。


### DataSource
MyBatis3.0内置连接池

dataSource type = POOLED启用连接池

数据库连接生命周期


### 连接池常用配置选项
#### poolMaximumActiveConnections
---数据库最大活跃连接数

---考虑到随着连接数的增加，性能可能达到拐点，不建议设置太大
#### poolMaximumIdleConnections
--最大空闲连接数
--经验值建议设置与poolMaximum形同即可。
#### poolMaximumCheckoutTime
--获取连接时如果没有idleConnection同时activeConnection达到最大值，则从activeConnections列表第一个连接开始，检查是否超过poolMaximumCheckoutTime，如果超过，则强制使其失效，返回该连接。

--由于SQL执行时间受服务器配置、表结构不同，建议设置为预期最大SQL执行时间。
#### poolTimeToWait
--获取服务器端数据库连接的超时时间，如果超过该时间，则打印日志，同时重新获取。
--建议使用默认值20s.
#### poolPingEnabled
--启用连接侦测，检查连接池中的连接是否为有效连接

--默认关闭，建议启用，放在服务器端异常关闭，导致客户端错误。
#### poolPingQuery
--侦测SQL，建议使用select1 开销小
#### poolPingConnectionNotUsedFor
--侦测时间，建议小于服务器端超时时间，MySQL默认超时8小时。
