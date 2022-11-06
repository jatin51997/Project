package com.Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TDG {
	private static Connection con;
	private static String jdbcUrl = "jdbc:sqlite:/Users/jatin/Desktop/workspace/java/APPproject/src/main/webapp/project.db";

	public ResultSet getDataFromDb(String query) {
		ResultSet rs = null;
		try {

			if (con == null) {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection(jdbcUrl);
			}

			Statement stmnt = con.createStatement();
			rs = stmnt.executeQuery(query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	public void insertDataInDb(String query) {
		try {
			if (con == null) {
				Class.forName("org.sqlite.JDBC");
				con = DriverManager.getConnection(jdbcUrl);
			}
			PreparedStatement stmt = con.prepareStatement(query);
			int records = stmt.executeUpdate();
			System.out.println(records + " records inserted");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
