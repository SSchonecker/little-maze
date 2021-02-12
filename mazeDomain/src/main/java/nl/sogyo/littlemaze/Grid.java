package nl.sogyo.littlemaze;

import java.util.Arrays;

/**
 * This class contains all the information on one Player object
 * and one maze, built from the Tile firstTile.
 * It is constructed with the maze size
 * or from a previous grid.
 */
public class Grid {
	
	private Tile firstTile;
	private Tile[][] theMaze;
	private Player myPlayer;
	private char[][][] mazeLayout;

	/**
	 * Constructor for a new random generated grid
	 * @param size: the size of the sides of the maze square 
	 */
	public Grid(int size) {
		if (size > 85) { // Too large grids may cause a StackOverflow
			throw new IndexOutOfBoundsException();
		}
		theMaze = new Tile[size][size];
		firstTile = new Tile(theMaze);
		makeMazeLayout();
	}
	
	/**
	 * Constructor for the regeneration of a grid
	 * @param playerName
	 * @param playerHealth
	 * @param playerSteps
	 * @param layout
	 */
	public Grid(String playerName, int playerHealth, int playerSteps, char[][][] layout) {
		buildMaze(layout);
		makeMazeLayout(); // Recreates the layout without the player in it
		int[] playerPos = playerInMaze(layout);
		firstTile = theMaze[0][0];
		firstTile.setMaze(theMaze);
		
		putPlayer(playerName, playerPos);
		myPlayer.setSteps(playerSteps);
		myPlayer.setHealth(playerHealth);
	}
	
	/**
	 * Method to build the maze from a layout matrix,
	 * by putting the correct type of tile on the theMaze matrix
	 * putting the chest with the right content and
	 * linking the neighbours.
	 * @param layout
	 */
	private void buildMaze(char[][][] layout) {
		theMaze = new Tile[layout.length][layout.length];
		for (int x = 0; x < layout.length; x++) {
			for (int y = 0; y < layout.length; y++) {
				// Putting in spikey tiles
				if (layout[x][y][0] == 's' || layout[x][y][0] == 'h') {
					theMaze[x][y] = new Spike(x, y);
					if (layout[x][y][0] == 'h') {
						theMaze[x][y].select();
					}
				}
				// Putting in normal tiles and the chest
				else {
					theMaze[x][y] = new Tile(x, y);
					if (layout[x][y][0] == 'c') {
						theMaze[x][y].putChest(5 * theMaze.length * theMaze.length);
					}
				}
			}
		}
		
		for (int x = 0; x < layout.length; x++) {
			for (int y = 0; y < layout.length; y++) {
				for (Direction dir : Direction.values()) {
					if (layout[x][y][dir.nr + 1] == '.') {
						theMaze[x][y].neighbours[dir.nr] = theMaze[x+dir.dx][y+dir.dy];
					}
				}
			}
		}
	}
	
	/**
	 * Get the position of the player from an old layout
	 * since the theMaze matrix does not contain the player position information
	 * @param layout
	 * @return the player position
	 */
	private int[] playerInMaze(char[][][] layout) {
		int[] playerPos = new int[2];
		for (int x = 0; x < layout.length; x++) {
			for (int y = 0; y < layout.length; y++) {
				if (layout[x][y][0] == 'p') {
					playerPos[0] = x;
					playerPos[1] = y;
					return playerPos;
				}
			}
		}
		return playerPos;
	}

	/**
	 * Method to create a matrix of the cells of the maze,
	 * on each cell saving the cell type (tile, chest, spike),
	 * and the object in each direction
	 * ('_' for a wall, '.' for an opening).
	 * This one is set once at the creation of a Grid and
	 * then during selection of a Tile, to represent the change in a selected spikey tile.
	 * 
	 * Makes grid[x][y]{type, North, East, South, West}
	 */
	private void makeMazeLayout() {
		mazeLayout = new char[theMaze.length][theMaze.length][5];
		for (int x = 0; x < theMaze.length; x++) {
			for (int y = 0; y < theMaze.length; y++) {
				Tile cell = theMaze[x][y];
				mazeLayout[x][y][0] = cell.type().charAt(0);
				for (int n = 1; n < 5; n++) { // The four directions
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
	
	/**
	 * Method to put a new player at a specified position
	 * @param playerName
	 * @param position
	 */
	public void putPlayer(String playerName, int[] position) {
		myPlayer = new Player(playerName);
		firstTile.getTileAt(position).moveTo(myPlayer);
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

	/**
	 * Method to return the player position after the
	 * maze has been generated, i.e. during the game
	 * @return player position
	 */
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
	 * Method to create a matrix copy of the cells of the maze,
	 * on each cell saving the cell type, and the object in each direction
	 * ('_' for a wall, '.' for an opening)
	 * and putting the player in the correct place.
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
	
	public int getSize() {
		return mazeLayout.length;
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

	/**
	 * Method to select a tile
	 * if it is next to the player and reachable (no wall)
	 * 
	 * @param i: x coordinate of tile
	 * @param j: y coordinate of tile
	 * @return whether the tile has been selected
	 */
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

	public int getChestContent() {
		int[] chestLoc = getChestLocation();
		Tile chestTile = theMaze[chestLoc[0]][chestLoc[1]];
		return chestTile.getContent();
	}

}

class InvalidKeyException extends Exception {

	private static final long serialVersionUID = 9061183126141731746L;

	public InvalidKeyException(String message) {
		super(message);
	}
	
}
