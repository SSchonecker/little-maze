package nl.sogyo.littlemaze.mazeapi.dtostructures;

/**
 * Class for the input from the start game page,
 * used in MazeInitiate
 * 
 */
public class PlayerInput {

	String playerName;
	String gridSize;

	public String getPlayerName() { return playerName; }

	public void setNameplayer(String playerName) {
		this.playerName = playerName;
	}

	public String getGridSize() { return gridSize; }

	public void setGridSize(String gridSize) {
		this.gridSize = gridSize;
	}
	
}
