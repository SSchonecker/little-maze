package nl.sogyo.littlemaze.mazeapi.dbconnect;

public class DataRow {
	
	private int userID;
	private String username;
	private String password;
	private String gameStateJSON;
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGameStateJSON() {
		return gameStateJSON;
	}

	public void setGameStateJSON(String gameStateJSON) {
		this.gameStateJSON = gameStateJSON;
	}

}
