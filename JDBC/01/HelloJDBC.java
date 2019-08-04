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
