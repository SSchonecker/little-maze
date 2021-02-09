package nl.sogyo.littlemaze;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import nl.sogyo.littlemaze.mazeapi.dbconnect.DataRow;
import nl.sogyo.littlemaze.mazeapi.dbconnect.SqlConnect;
import nl.sogyo.littlemaze.mazeapi.dtostructures.MazeDto;

class ConnectionTest {
	
	private String url = "jdbc:mysql://localhost:2220/test_maze";
	
	/**
	 * Check if the connection is set up by requesting the DB major version number
	 */
	@Test
	void createConnection() {
		int version = 0;
		try {
			SqlConnect myConnect = new SqlConnect(url);
			version = myConnect.makeConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			assertTrue(false);
		}
		assertEquals(8, version);
	}
	
	@Test
	void getUserWithName() {
		int ID = 0;
		String password = "";
		
		int expectedID = 1;
		String expectedPw = "something111111111111111111113111111111411111111151111111116111";
		
		try {
			SqlConnect myConnect = new SqlConnect(url);
			DataRow data = myConnect.getUserInfo("someone");
			ID = data.getUserID();
			password = data.getPassword();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			assertTrue(false);
		}
		
		assertEquals(expectedID, ID);
		assertEquals(expectedPw, password);
	}
	
	@Test
	void enterNewUser() {
		String newName = "someguy";
		String newPassword = "somepassword";
		
		int ID = 0;
		String password = "";
		
		try {
			SqlConnect myConnect = new SqlConnect(url);
			myConnect.addUser(newName, newPassword);
			DataRow data = myConnect.getUserInfo(newName);
			ID = data.getUserID();
			password = data.getPassword();
			myConnect.removeTestUser();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			assertTrue(false);
		}
		
		assertNotEquals(0, ID);
		assertEquals(newPassword, password);
		
	}
	
	@Test
	void getErrorOnWrongUserRequest() {
		
		assertThrows(SQLException.class, () -> {
			SqlConnect myConnect = new SqlConnect(url);
			myConnect.getUserInfo("non-existing-p");
		});
	}
	
	@Test
	void saveAGame() {
		Grid mazeGrid = new Grid(2);
		mazeGrid.putPlayer("someone");	
		MazeDto gameState = new MazeDto(mazeGrid, "someone");
		
		try {
			SqlConnect myConnect = new SqlConnect(url);
			myConnect.saveGame(gameState, "someone", "1");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			assertTrue(false);
		}
		
		String savedGame = "";
		
		try {
			SqlConnect myConnect = new SqlConnect(url);
			DataRow data = myConnect.getUserInfo("someone");
			savedGame = data.getGameStateJSON("1");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			assertTrue(false);
		}
		assertNotNull(savedGame);
	}
	
	@Test
	void loadAGame() {
		
		String loadedGame = "";
		try {
			SqlConnect myConnect = new SqlConnect(url);
			loadedGame = myConnect.loadGame("someone", "1");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			assertTrue(false);
		}
		
		assertNotNull(loadedGame);
	}
}
