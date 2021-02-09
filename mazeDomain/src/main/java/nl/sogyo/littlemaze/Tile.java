package nl.sogyo.littlemaze;

import java.util.Arrays;
import java.util.Collections;

class Tile {
	
	private int[] position = new int[2];
	private boolean revealed = false;
	Tile[] neighbours = new Tile[4]; // Lists the neighbours according to the direction they lie in
	private boolean hasChest = false;
	private static final int TREASURE = 100; // Value of the chest content, required for the score
	private Tile[][] maze;

	/**
	 * Constructor for a single tile
	 */
	public Tile(int x, int y) {
		position[0] = x;
		position[1] = y;
	}
	
	/**
	 * Constructor for the first tile of a mazeGrid.
	 * Saves the grid and creates its first neighbour to the EAST
	 */
	public Tile(Tile[][] mazeGrid) {
		mazeGrid[0][0] = this;
		this.maze = mazeGrid;
		position[0] = 0;
		position[1] = 0;
		neighbours[Direction.SOUTH.nr] = new Tile(mazeGrid, Direction.SOUTH, this); 
	}
	
	/**
	 * Constructor for maze internal tiles.
	 * Saves the previous tile as neighbour
	 * and builds the following tiles through depth-first search (see README).
	 */
	public Tile(Tile[][] mazeGrid, Direction fromLast, Tile last) {
		this.position[0] = last.getPosition()[0] + fromLast.dx;
		this.position[1] = last.getPosition()[1] + fromLast.dy;
		mazeGrid[position[0]][position[1]] = this;
		neighbours[fromLast.opposite.nr] = last;
		
		Direction[] dirs = Direction.values();
		Collections.shuffle(Arrays.asList(dirs)); // Try branching along random directions
		for (Direction dir : dirs) {
			int nx = position[0] + dir.dx;
			int ny = position[1] + dir.dy; // Pick a new cell in this direction
			if (between(nx, mazeGrid.length) && between(ny, mazeGrid.length)
					&& (mazeGrid[nx][ny] == null)) { // Ensure this cell is within the grid and empty
				if (nx == ny) { //TODO Place spikes randomly?
					neighbours[dir.nr] = new Spike(mazeGrid, dir, this); // Put spikes on the diagonals
				}
				else {
					neighbours[dir.nr] = new Tile(mazeGrid, dir, this);
				}
			}
			
			if (shouldPutChest(mazeGrid)) {
				this.hasChest = true;
			}
		}
	}

	/**
	 * Method to determine whether a tile should put a chest on itself.
	 * Only returns true if the whole maze has been filled and no other tile has a chest
	 */
	private boolean shouldPutChest(Tile[][] mazeGrid) {
		for (int i = (mazeGrid.length - 1); i >= 0; i = i-1) {
			for (int j = (mazeGrid.length - 1); j >= 0; j = j-1) {
				if (mazeGrid[i][j] == null || mazeGrid[i][j].hasChest) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean between(int value, int max) {
		return (value >= 0) && (value < max);
	}

	public int[] getPosition() {
		return position;
	}

	public void select() {
		revealed = true;
	}
	
	/**
	 * Method to request the tiles type.
	 * 
	 * @return "t" for tile or "c" for chest
	 */
	public String type() {
		if (hasChest) {
			return "c";
		}
		return "t";
	}

	public boolean isChecked() {
		return revealed;
	}

	/**
	 * Method to put the player on this tile.
	 * Ends the game if it contains the chest, by ensuring the player can no longer move
	 * 
	 * @param The player to be put on this tile
	 */
	public void moveTo(Player aPlayer) {
		aPlayer.putHere(this);
		if (hasChest) {
			aPlayer.setScore(TREASURE);
			Arrays.fill(this.neighbours, null); // Remove all neighbours, so player can't keep walking
		}
	}

	/**
	 * Method to access a tile at a particular set of x and y coordinates in the maze.
	 *
	 * @return tile in the maze at x, y
	 */
	public Tile getTileAt(int x, int y) {
		if (between(x, maze.length) && between (y, maze.length)) {
			return maze[x][y];
		}
		return null; 
	}
	
	/**
	 * Method to access a tile at a particular point in the maze.
	 * 
	 * @return tile in the maze at that point
	 */
	public Tile getTileAt(int[] position) {
		return getTileAt(position[0], position[1]);
	}
	
	public void setMaze(Tile[][] aMaze) {
		this.maze = aMaze;
	}

	/**
	 * Method to try to move the player to the next neighbour.
	 * Only forwards the player if he can move in the particular direction
	 * 
	 * @param dir Direction the player wants to move toward
	 * @param aPlayer The player to be moved to the neighbour
	 */
	public void walkTo(Direction dir, Player aPlayer) {
		if (neighbours[dir.nr] != null) {
			neighbours[dir.nr].moveTo(aPlayer);
		}
	}

	public boolean containsChest() {
		return hasChest;
	}
	
	public void putChest() {
		this.hasChest = true;
	}

	public Tile getNeighbour(int n) {
		return neighbours[n];
	}

}
