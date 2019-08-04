### JDBC构建步骤：
装载驱动程序

建立数据库连接

执行SQL语句

获取执行结果

清理环境
```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloJDBC {
	static final String JDBC_DRIVER = "com.mysql.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/cloud_study";
	static final String USER = "root";
	static final String PASSWORD = "123456";
	
	public static void helloword() throws ClassNotFoundException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		//1.装载驱动程序
		Class.forName(JDBC_DRIVER);
		//2.建立数据库连接
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASSWORD);
			//3.执行SQL语句
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select userName from user");
			//4.获取执行结果
			while(rs.next()) {
				System.out.println("Hello" + rs.getString("userName"));
			}	
			
		} catch (SQLException e) {
			// 异常处理
			e.printStackTrace();
		}finally {
			//5.清理环境
			try {
				if(conn != null)	
				    conn.close();
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				// ignore
			}
		}
	}

	public static void main(String[] args) {
		helloword();

	}

}
```
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
```java
//游标的方式读取数据库表
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloJDBC {
	static final String JDBC_DRIVER = "com.mysql.Driver";
	static String DB_URL = "jdbc:mysql://localhost/cloud_study";
	static final String USER = "root";
	static final String PASSWORD = "123456";
	
	public static void helloword() throws ClassNotFoundException{
		Connection conn = null;
		Statement stmt = null;
    
		PreparedStatement ptmt = null;
    
		ResultSet rs = null;
		
		//1.装载驱动程序
		Class.forName(JDBC_DRIVER);
		//2.建立数据库连接
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASSWORD);
			DB_URL = DB_URL + "userCursorFetch=ture";
			//3.执行SQL语句
      
      
			ptmt = conn.prepareStatement("select userName from user");
			ptmt.setFetchSize(1);//每次读取一条记录，然后读取下一条。
			rs = ptmt.executeQuery();
			
      
			//4.获取执行结果
			while(rs.next()) {
				System.out.println("Hello" + rs.getString("userName"));
			}	
			
		} catch (SQLException e) {
			// 异常处理
			e.printStackTrace();
		}finally {
			//5.清理环境
			try {
				if(conn != null)	
				    conn.close();
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				// ignore
			}
		}
	}

	public static void main(String[] args) {
		helloword();

	}

}
```
### 业务场景三：每条记录中存在大字段内容（如博客、图片）
使用流方式：
```java
try {
              //1.加载数据库驱动
              class.forName(JDBC_DRIVER);
              //2.获取数据库连接
	      conn = DriverManager.getConnection(DB_URL,USER,PASS);
              //3.创建PreparementStatement对象
	      String sql = “select * from user_note”;
	      ptmt = conn.prepareStatement(sql);
              //4.执行SQL
	      rs = ptmt.executeQuery();
	      while(rs.next()) { 

//5.获取对象流
InputSteam in = rs.getBinaryStream(“blog”);
//6.将对象流写入文件
File f = new(FILE_URL);
OutputStream out = null;
out = new FileOutputStream(f);
int temp = 0;
While((temp = in.read())!=-1){ //边读边写
      out.write(temp);
     }	
     in.close();    
     out.close();
     } catch (SQLException e) {
```
### 业务场景四：海量数据插入操作
批处理——发送一次SQL插入多条数据

Statement

--addBatch()

--executeBatch()

--clearBatch()

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class HelloJDBC {
	static final String JDBC_DRIVER = "com.mysql.Driver";
	static String DB_URL = "jdbc:mysql://localhost/cloud_study";
	static final String USER = "root";
	static final String PASSWORD = "123456";
	
	public static void insertUsers(Set<String> users) throws ClassNotFoundException{
		Connection conn = null;
		Statement stmt = null;
		
		//1.装载驱动程序
		Class.forName(JDBC_DRIVER);
		//2.建立数据库连接
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASSWORD);

			//3.执行SQL语句
			stmt = conn.createStatement();
			for(String user:users) {
				stmt.addBatch("insert into user(userName) vlaues("+ user+")");
			}
			stmt.executeBatch();
			stmt.clearBatch();

		} catch (SQLException e) {
			// 异常处理
			e.printStackTrace();
		}finally {
			//5.清理环境
			try {
				if(conn != null)	
				    conn.close();
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				// ignore
			}
		}
	}

	public static void main(String[] args) {
		Set<String> users = new HashSet<String>();
		users.add("GuoYi");
		users.add("ZhangSi");
		users.add("LiSan");
		insertUsers(users);

	}

}

```
### 业务场景五：中文问题
JDBC编码的设置:
DB_URL = DB_URL + characterEncoding=utf8
