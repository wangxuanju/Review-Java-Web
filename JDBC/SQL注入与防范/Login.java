import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {
	public final static String JDBC_DRIVER = "com.mysql.Driver";
	public final static String USER = "root";
	public final static String PASSWORD = "123456";
	public final static String DB_URL = "jdbc:mysql://localhost/cloud_study";
	
	public static User login(String userName,String password)
			throws ClassNotFountException{
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		User user = null;
		
		//1.装载驱动程序
		Class.forName(JDBC_DRIVER);
		//2.建立数据库连接
		try {
			conn = DriverManager.getConnection(DB_URL,USER,PASSWORD);

			//3.执行SQL语句
			ptmt = conn.prepareStatement("select * from user where userName=? and password=?");
			ptmt.setString(1,userName);
			ptmt.setString(2,password);
			
			rs = ptmt.executeQuery();
			while(rs.next()) {
				user=new User();
				user.setUserName(rs.getString("userName"));
				user.setSex(rs.getBoolean("sex"));
				}
			} catch (SQLException e) {
				// 异常处理
				e.printStackTrace();
			}finally {
				//5.清理环境
				try {
					if(conn != null)	
						   conn.close();
					if(ptmt != null)
						ptmt.close();
					if(rs != null)
						rs.close();
				} catch (SQLException e) {
						// ignore
				}
			}
	}

	public static void main(String[] args)throws ClassNotFoundException {
		System.out.println(login("ZhangSi","12345")!=null);
		

	}

}
