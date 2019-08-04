import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBPoolDbcpImpl extends Thread {
public static BasicDataSource  ds = null;
	
	public final static String DRIVER_NAME = "com.mysql.Driver";
	public final static String USER_NAME = "root";
	public final static String PASSWORD = "123456";
	public final static String DB_URL = "jdbc:mysql://localhost/cloud_study";
	
	public static void dbpoolInit() {
		ds = new BasicDataSource();
	    ds.setDriverClassName(DRIVER_NAME);
		ds.setUrl(DB_URL);
		ds.setUserName(USER_NAME);
		ds.setPassword(PASSWORD);
		ds.setMaxTotal(2);
	}
	public void dbPoolTest() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			//3.执行SQL语句
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from user");
			while(rs.next()) {
				System.out.println(rs.getString("userName"));
			}	
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn != null) 
					conn.close();
				if(stmt != null) 
					stmt.close();
				if(rs != null) 
					rs.close();
			}catch(SQLException e1) {
				//ignore
			}
			
		}
		
	}
	public void jdbcTest() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(DB_URL,USER_NAME,PASSWORD);
			//3.执行SQL语句
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from user");
			while(rs.next()) {
				System.out.println(rs.getString("userName"));
			}	
		}catch(SQLException e) {
			//ignore
		}finally {
			try {
				if(conn != null) 
					conn.close();
				if(stmt != null) 
					stmt.close();
				if(rs != null) 
					rs.close();
			}catch(SQLException e1) {
				//ignore
			}
			
		}
		
	}
	public void run() {
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis() - start<10000) {
			dbPoolTest();
		}
	}
	

	public static void main(String[] args) {
		dbpoolInit();
		for(int i = 0;i < 10;i++) {
			new DBPoolDbcpImpl().start();
		}
	}
}
