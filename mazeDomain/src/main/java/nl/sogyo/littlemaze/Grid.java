package nl.sogyo.littlemaze;

import java.util.Arrays;

/**
 * This class contains all the information on one Player object
 * and one maze, built from the firstTile.
 * It is constructed with the maze size.
 */
public class Grid {
	
	private Tile firstTile;
	private Tile[][] theMaze;
	private Player myPlayer;
	private char[][][] mazeLayout;

	public Grid(int size) {
		if (size > 85) { // Too large grids may cause a StackOverflow
			throw new IndexOutOfBoundsException();
		}
		theMaze = new Tile[size][size];
		firstTile = new Tile(theMaze);
		makeMazeLayout();
	}
	
	private void makeMazeLayout() {
		mazeLayout = new char[theMaze.length][theMaze.length][5];
		for (int x = 0; x < theMaze.length; x++) {
			for (int y = 0; y < theMaze.length; y++) {
				Tile cell = theMaze[x][y];
				mazeLayout[x][y][0] = cell.type().charAt(0);
				for (int n = 1; n < 5; n++) {
					if(cell.getNeighbour(n-1) == null) {
						mazeLayout[x][y][n] = '_';
					}
					else {
						mazeLayout[x][y][n] = '.';
					}
				}
			}
		}
	}
	
	/**
	 * Method to put a new player at the entrance of the maze
	 * 
	 * @param playerName The player's name
	 */
	public void putPlayer(String playerName) {
		myPlayer = new Player(playerName);
		firstTile.moveTo(myPlayer);
	}

	public String getPlayerName() {
		if (myPlayer != null) {
			return myPlayer.getName();
		}
		return null;
	}
	
	/**
	 * Method to find out if the game has ended.
	 * 
	 * @return false if the game has not ended yet, true if it has.
	 */
	public boolean getGameStatus() {
		if (myPlayer != null) {
			return myPlayer.isGameOver();
		}
		return true;
	}

	public int getScore() {
		if (myPlayer != null) {
			return myPlayer.getScore();
		}
		return 0;
	}

	public int[] getPlayerLocation() {
		if (myPlayer != null) {
			return myPlayer.getPosition();
		}
		return null;
	}

	public int[] getChestLocation() {
		for (Tile[] row : theMaze) {
			for (Tile aTile : row ) {
				if (aTile.containsChest()) {
					return aTile.getPosition();
				}
			}
		}
		return null;
	}

	/**
	 * Method to create a matrix of the cells of the maze,
	 * on each cell saving the cell type, and the object in each direction
	 * ('_' for a wall, '.' for an opening)
	 * 
	 * @return grid[x][y]{type, North, East, South, West}
	 */
	public char[][][] getLayout() {
		char[][][] currentMaze = new char[mazeLayout.length][mazeLayout.length][5];  
		for(int i = 0; i < mazeLayout.length; i++) {
			for(int j=0; j < mazeLayout[i].length; j++) {
				System.arraycopy(mazeLayout[i][j], 0, currentMaze[i][j], 0, 5);
			}
		}
		currentMaze[myPlayer.getPosition()[0]][myPlayer.getPosition()[1]][0] = 'p';
		return currentMaze;
	}

	public int getSteps() {
		if (myPlayer != null) {
			return myPlayer.getSteps();
		}
		return 0;
	}

	public int getPlayerHealth() {
		if (myPlayer != null) {
			return myPlayer.getHealth();
		}
		return 0;
	}

	public void stirPlayer(String key) throws InvalidKeyException {
		switch (key.toLowerCase()) {
		case("w"):
			myPlayer.moveUpward();
			break;
		case("s"):
			myPlayer.moveDownward();
			break;
		case("q"):
			myPlayer.turnLeft();
			break;
		case("e"):
			myPlayer.turnRight();
			break;
		case("a"):
			myPlayer.moveLeft();
			break;
		case("d"):
			myPlayer.moveRight();
			break;
		default:
			throw new InvalidKeyException("Not a valid key.");
		}
	}

	public String getPlayerOrientation() {
		if (myPlayer != null) {
			return myPlayer.getOrientation().toString();
		}
		return null;
	}

	public boolean selectTile(int i, int j) {
		int[] target = {i, j};
		for (int n = 0; n < 4; n++) {
			Tile aNeighbour = firstTile.getTileAt(myPlayer.getPosition()).getNeighbour(n);
			if (aNeighbour != null 
					&& Arrays.equals(target, aNeighbour.getPosition())) {
				aNeighbour.select();
				makeMazeLayout();
				return true;
			}
		}
		return false;
	}

	public boolean getTileRevealed(int x, int y) {
		return theMaze[x][y].isChecked();
	}

	public String getTileType(int x, int y) {
		return theMaze[x][y].type();
	}
}

class InvalidKeyException extends Exception {

	private static final long serialVersionUID = 9061183126141731746L;

	public InvalidKeyException(String message) {
		super(message);
	}
	
}
