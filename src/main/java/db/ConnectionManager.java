package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

	private static final String database = "variamosDB";
	private static final String url = "jdbc:mysql://localhost:3306/" + database;
	private static final String driverName = "com.mysql.cj.jdbc.Driver";
	private static final String username = "root";
	private static final String password = "admin123";
	private static Connection conn;

	public static Connection getConnection() {
		try {
			Class.forName(driverName);
			try {
				conn = DriverManager.getConnection(url, username, password);
			} catch (SQLException ex) {
				System.out.println("Failed to create the database connection.");
			}
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver not found.");
		}
		return conn;
	}

}
