package nl.sogyo.littlemaze.mazeapi.dbconnect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sogyo.littlemaze.mazeapi.dtostructures.MazeDto;

/**
 * Class to handle all communication with the MySQL database.
 * The DB user has limited rights.
 *
 */
public class SqlConnect {
	
	private String dbUser = "mazewalker";
	private String dbpw = "runT0mrun!";
	private String url = "";
	private String schema = "";
	
	public SqlConnect(String url) {
		this.url = url; // The url contains the full address to the DB
		schema = url.split("/")[3]; // The schema/db is only the last part of the url 
	}
	
	/**
	 * Method to test the connection to the database
	 * @return the major version number of the database
	 * @throws SQLException
	 */
	public int makeConnection() throws SQLException {
		int version = 0;
		try (
			Connection conn = DriverManager.getConnection(url, dbUser, dbpw);
		) {
			DatabaseMetaData data = conn.getMetaData();
			version = data.getDatabaseMajorVersion();
		}
		return version;
	}

	/**
	 * Method to retrieve the logged in user's information
	 * @param name of the FE user wanting to log in
	 * @return the DataRow object containing the data from the user's row
	 * @throws SQLException
	 */
	public DataRow getUserInfo(String name) throws SQLException {
		DataRow data = new DataRow();
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try (
			Connection conn = DriverManager.getConnection(url,dbUser,dbpw);
		) {
			stmt = conn.prepareStatement("SELECT * FROM " + schema + ".user WHERE userName=?");
			stmt.setString(1, name);
			resultSet = stmt.executeQuery();
			
			resultSet.next();
			data.setUserID(resultSet.getInt("userID"));
			data.setUsername(resultSet.getString("username"));
			data.setPassword(resultSet.getString("password"));
			data.setGameStateJSON(resultSet.getString("savedgameone"));
			data.setGameStateJSONtwo(resultSet.getString("savedgametwo"));
		}
		finally {
			if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
			if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		}
		return data;
	}

	/**
	 * Method to add a new FE user to the DB
	 * @param newName
	 * @param newPassword the hashed password
	 * @throws SQLException
	 */
	public void addUser(String newName, String newPassword) throws SQLException {
		PreparedStatement stmt = null;
		try (
			Connection conn = DriverManager.getConnection(url,dbUser,dbpw);
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

	/**
	 * Method to save a game for a FE user into one of two game slots
	 * @param gameState
	 * @param username
	 * @param saveSlot
	 * @throws SQLException
	 */
	public void saveGame(MazeDto gameState, String username, String saveSlot) throws SQLException {
		PreparedStatement stmt = null;
		ObjectMapper mapper = new ObjectMapper(); 
		String jsonResult = "";
		try {
			jsonResult = mapper.writeValueAsString(gameState);
		} catch (JsonProcessingException e) {
			throw new SQLException(e.getMessage());
		}
		
		try (
			Connection conn = DriverManager.getConnection(url,dbUser,dbpw);
		) {
			if (saveSlot.equals("1")) {
				stmt = conn.prepareStatement("UPDATE " + schema + ".user SET savedgameone = ? WHERE userName=?;");
			}
			else {
				stmt = conn.prepareStatement("UPDATE " + schema + ".user SET savedgametwo = ? WHERE userName=?;");
			}
			stmt.setString(1, jsonResult);
			stmt.setString(2, username);
			stmt.execute();
		}
		finally {
			if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		}
	}
	
	/**
	 * Method to delete a user in the test schema.
	 * Note that the permission to delete is only granted to dbUser
	 * on the test_maze db, not the normal one.
	 * @throws SQLException
	 */
	public void removeTestUser() throws SQLException {
		PreparedStatement stmt = null;
		try (
			Connection conn = DriverManager.getConnection(url,dbUser,dbpw);
		) {
			stmt = conn.prepareStatement("DELETE FROM `test_maze`.`user` WHERE (`username` = 'someguy');");
			stmt.execute();
		}
		finally {
			if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		}
	}

	/**
	 * Method to get the json string game state from a slot in the 
	 * user information row.
	 * @param userName
	 * @param slot the save slot to get the game from.
	 * @return json string containing the game state
	 * @throws SQLException
	 */
	public String loadGame(String userName, String slot) throws SQLException {
		DataRow userData = null;
		String loadedGame = "";
		try {
			userData = getUserInfo(userName);
			loadedGame = userData.getGameStateJSON(slot);
		} catch (SQLException err) {
			throw new SQLException(err);
		}
		return loadedGame;
	}

	/**
	 * Method to return a sorted list of rows from the score table
	 * for a particular user's name
	 * @param name The user's name
	 * @param newScore The user's latest score
	 * @return List<ScoreRow> Sorted list of rows of score data
	 * @throws SQLException 
	 */
	public List<ScoreRow> getScores(String name, int newScore, int gridSize) throws SQLException {
		List<ScoreRow> scoreRows = new ArrayList<>(5);
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try (
			Connection conn = DriverManager.getConnection(url,dbUser,dbpw);
		) {
			stmt = conn.prepareStatement("SELECT * FROM " + schema + ".score WHERE (userID = (SELECT userID FROM " + schema + ".user WHERE username=?));");
			stmt.setString(1, name);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				ScoreRow aRow = new ScoreRow();
				aRow.setScoreID(resultSet.getInt("scoreID"));
				aRow.setScorevalue(resultSet.getInt("scorevalue"));
				aRow.setGridSize(resultSet.getInt("gridsize"));
				aRow.setDatetime(resultSet.getTimestamp("date"));
				aRow.setUserID(resultSet.getInt("userID"));
				scoreRows.add(aRow);
			}
		}
		finally {
			if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
			if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		}

		List<ScoreRow> sortedScores = scoreRows.stream()
				.sorted(Comparator.comparing(ScoreRow::getScorevalue).reversed())
				.collect(Collectors.toList());
		
		/* If the new score is lower than the lowest previous one, don't store it */
		if (sortedScores.size() >= 5 && newScore < sortedScores.get(sortedScores.size() - 1).getScorevalue()) {
			return sortedScores;
		}
		
		/* The new score needs to be added to the table */
		ScoreRow newRow = null;

		if (sortedScores.size() >= 5) {
			newRow = updateScores(newScore, sortedScores.get(sortedScores.size() - 1).getScoreID(), gridSize);
			sortedScores.remove(sortedScores.size() - 1); // Remove the last score
		}
		else {
			newRow = addToScores(newScore, gridSize, name);
		}
		
		sortedScores.add(newRow);
		return sortedScores.stream()
				.sorted(Comparator.comparing(ScoreRow::getScorevalue).reversed())
				.collect(Collectors.toList()); // Sort the scores again with the new score added
	}

	/**
	 * Method to remove an old score and place a new one
	 * using a stored procedure in MySQL
	 * @param newScore
	 * @param oldScoreID
	 * @param gridSize
	 * @return the row of data for the new score
	 * @throws SQLException
	 */
	private ScoreRow updateScores(int newScore, int oldScoreID, int gridSize) throws SQLException {
		PreparedStatement stmt = null;
		ScoreRow aRow = new ScoreRow();
		try (
			Connection conn = DriverManager.getConnection(url,dbUser,dbpw);
		) {
			stmt = conn.prepareStatement("CALL " + schema + ".UpdateScore(?, ?, ?);");
			stmt.setLong(1, newScore);
			stmt.setLong(2, gridSize);
			stmt.setLong(3, oldScoreID);
			ResultSet resultSet = stmt.executeQuery();
			resultSet.next();
			aRow.setScoreID(resultSet.getInt("scoreID"));
			aRow.setScorevalue(resultSet.getInt("scorevalue"));
			aRow.setGridSize(resultSet.getInt("gridsize"));
			aRow.setDatetime(resultSet.getTimestamp("date"));
			aRow.setUserID(resultSet.getInt("userID"));
		}
		finally {
			if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
		}
		return aRow;
	}
	
	/**
	 * Method to add a new score to the scores table in the DB
	 * @param score
	 * @param gridSize
	 * @param name
	 * @return the row of data for the new score
	 * @throws SQLException
	 */
	private ScoreRow addToScores(int score, int gridSize, String name) throws SQLException {
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ScoreRow aRow = new ScoreRow();
		try (
			Connection conn = DriverManager.getConnection(url,dbUser,dbpw);
		) {
			stmt = conn.prepareStatement("INSERT INTO " + schema + ".score (scorevalue, gridsize, date, userID) VALUES (?, ?, NOW(), (SELECT userID FROM "+ schema + ".user WHERE username=?));");
			stmt.setLong(1, score);
			stmt.setLong(2, gridSize);
			stmt.setString(3, name);
			stmt.execute();
			
			stmt2 = conn.prepareStatement("SELECT * FROM " + schema + ".score WHERE scoreID=(SELECT max(scoreID) FROM " + schema + ".score WHERE scorevalue=? AND gridsize=?);");
			stmt2.setLong(1, score);
			stmt2.setLong(2, gridSize);
			ResultSet resultSet = stmt2.executeQuery();
			resultSet.next();
			aRow.setScoreID(resultSet.getInt("scoreID"));
			aRow.setScorevalue(resultSet.getInt("scorevalue"));
			aRow.setGridSize(resultSet.getInt("gridsize"));
			aRow.setDatetime(resultSet.getTimestamp("date"));
			aRow.setUserID(resultSet.getInt("userID"));
		}
		finally {
			if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
			if (stmt2 != null) try { stmt2.close(); } catch (SQLException ignore) {}
		}
		return aRow;
	}
}
