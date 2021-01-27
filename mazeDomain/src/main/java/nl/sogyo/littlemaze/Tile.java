package nl.sogyo.littlemaze;

import java.util.Arrays;
import java.util.Collections;

class Tile {
	
	private int[] position = new int[2];
	private boolean revealed = false;
	Tile[] neighbours = new Tile[4];
	private boolean hasChest = false;
	private static final int TREASURE = 100;
	private Tile[][] maze;

	public Tile(int x, int y) {
		position[0] = x;
		position[1] = y;
	}
	
	public Tile(Tile[][] mazeGrid) {
		mazeGrid[0][0] = this;
		this.maze = mazeGrid;
		position[0] = 0;
		position[1] = 0;
		neighbours[Direction.EAST.nr] = new Tile(mazeGrid, Direction.EAST, this); 
	}
	
	public Tile(Tile[][] mazeGrid, Direction fromLast, Tile last) {
		this.position[0] = last.getPosition()[0] + fromLast.dx;
		this.position[1] = last.getPosition()[1] + fromLast.dy;
		mazeGrid[position[0]][position[1]] = this;
		neighbours[fromLast.opposite.nr] = last;
		
		Direction[] dirs = Direction.values();
		Collections.shuffle(Arrays.asList(dirs));
		for (Direction dir : dirs) {
			int nx = position[0] + dir.dx;
			int ny = position[1] + dir.dy;
			if (between(nx, mazeGrid.length) && between(ny, mazeGrid.length)
					&& (mazeGrid[nx][ny] == null)) {
				if (nx == ny) {
					neighbours[dir.nr] = new Spike(mazeGrid, dir, this);
				}
				else {
					neighbours[dir.nr] = new Tile(mazeGrid, dir, this);
				}
			}
			if (shouldPutchest(mazeGrid)) {
				this.hasChest = true;
			}
		}
	}

	private boolean shouldPutchest(Tile[][] mazeGrid) {
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
		if (hasChest) {
			System.out.println("You found the chest!");
		}
		else {
			System.out.println("This looks like an ordinary tile.");
		}
		revealed = true;
	}

	public boolean isChecked() {
		return revealed;
	}

	public void moveTo(Player aPlayer) {
		aPlayer.putHere(this);
		if (hasChest) {
			aPlayer.setScore(TREASURE);
			Arrays.fill(this.neighbours, null);
		}
	}

	public Tile getTileAt(int i, int j) {
		if (between(i, maze.length) && between (j, maze.length)) {
			return maze[i][j];
		}
		return null; 
	}

	public void walkTo(Direction dir, Player aPlayer) {
		if (neighbours[dir.nr] != null) {
			neighbours[dir.nr].moveTo(aPlayer);
		}
		else {
			System.out.println("You can't move there.");
		}
	}

	public boolean containsChest() {
		return hasChest;
	}

	public Tile getNeighbour(int n) {
		return neighbours[n];
	}

}
