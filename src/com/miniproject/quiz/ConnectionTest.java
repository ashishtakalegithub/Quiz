package com.miniproject.quiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {
	Connection con = null;

	// connection with database
	public Connection getConnectionDetails() {
		String url = "jdbc:mysql://localhost:3306/student";
		String user = "root";
		String pwd = "root";
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, pwd);
			//Directly use the parent exception here 
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return con;

	}
}
