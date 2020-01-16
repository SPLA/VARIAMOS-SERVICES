package db;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class Pool {

	private final String DRIVER = "com.mysql.cj.jdbc.Driver";
	public DataSource dataSource;
	
	public final String dataBase = "variamosDB";
	public final String url = "jdbc:mysql://localhost:3306/" + dataBase + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public final String userName = "root";
	public final String password = "1234";
	
	public Pool() {
		startDataSource();
	}
	
	private void startDataSource() {
		
		BasicDataSource ds = new BasicDataSource();
		
		ds.setDriverClassName(DRIVER);
		ds.setUsername(userName);
		ds.setPassword(password);
		ds.setUrl(url);
		
		dataSource = ds;
	}
}
