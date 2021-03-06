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
