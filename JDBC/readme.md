### JDBC构建步骤：
装载驱动程序

建立数据库连接

执行SQL语句

获取执行结果

清理环境
### 业务场景一：
Select * from user where set=”男”
过滤条件太弱，一次读出过多的数据库记录。
### 业务场景二：读取数据库表中所有的记录
JAVA内存可能溢出，因为JVM有内存大小限制，如果超过了最大内存限制，就会有内存溢出的异常。

解决办法：每次读出一部分数据，然后下次再读一部分数据…

#### 游标提供一种客户端读取部分服务器端结果集的机制(允许分批读取数据)
使用游标：DB_URL(userCursorFetch=true来开启游标)
```java
PreparedStatement ptmt = null;
String sql = “select * from user where sex = ?”;
ptmt = conn.prepareStatement(sql);
ptmt.setFetchSize(10);
ptmt.setString(1,”男”);
rs = ptmt.executeQuery();
```
