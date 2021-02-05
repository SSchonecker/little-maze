package nl.sogyo.littlemaze.mazeapi.dbconnect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import nl.sogyo.littlemaze.mazeapi.dbconnect.DataRow;

public class SqlConnect {
	
	private String user = "mazewalker";
	private String dbpw = "runT0mrun!";
	private String url = "";
	private String schema = "";
	
	public SqlConnect(String url) {
		this.url = url;
		schema = url.split("/")[3];
	}
	
	public int makeConnection() throws SQLException {
		int version = 0;
		try (
			Connection conn = DriverManager.getConnection(url, user, dbpw);
		) {
			DatabaseMetaData data = conn.getMetaData();
			version = data.getDatabaseMajorVersion();
		}
		return version;
	}

	public DataRow getUserInfo(String name) throws SQLException {
		DataRow data = new DataRow();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try (
			Connection conn = DriverManager.getConnection(url,user,dbpw);
		) {
			stmt = conn.prepareStatement("SELECT * FROM " + schema + ".user WHERE userName=?");
			stmt.setString(1, name);
			resultSet = stmt.executeQuery();
			resultSet.next();
			data.setUserID(resultSet.getInt("userID"));
			data.setUsername(resultSet.getString("username"));
			data.setPassword(resultSet.getString("password"));
		}
		finally {
			if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
			if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		}
		return data;
	}

	public void addUser(String newName, String newPassword) throws SQLException {
		
		PreparedStatement stmt = null;
		try (
			Connection conn = DriverManager.getConnection(url,user,dbpw);
		) {
			stmt = conn.prepareStatement("INSERT INTO " + schema + ".user (username, password) VALUES (?, ?);");
			stmt.setString(1, newName);
			stmt.setString(2, newPassword);
			stmt.execute();
		}
		finally {
			if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		}
		
	}

	public void removeUser() throws SQLException {
		PreparedStatement stmt = null;
		try (
			Connection conn = DriverManager.getConnection(url,user,dbpw);
		) {
			stmt = conn.prepareStatement("DELETE FROM `test_maze`.`user` WHERE (`username` = 'someguy');");
			stmt.execute();
		}
		finally {
			if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		}
	}
	
}
