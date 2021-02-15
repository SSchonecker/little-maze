package nl.sogyo.littlemaze.mazeapi.dbconnect;

/**
 * Class that represents a row in the user-table of the DB
 *
 */
public class DataRow {
	
	private int userID;
	private String username;
	private String password;
	private String gameStateJSON;
	private String gameStateJSONtwo;
	
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

	public String getGameStateJSON(String slot) {
		if(slot.equals("1")) {
			return gameStateJSON;
		}
		else {
			return gameStateJSONtwo;
		}
	}

	public void setGameStateJSON(String gameStateJSON) {
		this.gameStateJSON = gameStateJSON;
	}
	
	public void setGameStateJSONtwo(String gameStateJSONtwo) {
		this.gameStateJSONtwo = gameStateJSONtwo;
	}

	/**
	 * Method to indicate how many of the save slots are used
	 * @return int the number of slots used
	 */
	public int slotsUsed() {
		int slotsUsed = 0;
		if (gameStateJSON != null) {
			slotsUsed += 1;
		}
		if (gameStateJSONtwo != null) {
			slotsUsed += 1;
		}
		return slotsUsed;
	}

}
