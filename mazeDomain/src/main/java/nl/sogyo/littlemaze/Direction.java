package nl.sogyo.littlemaze;

/**
 * Enum for the four directions of the maze.
 * The directions are numbered (.nr) for list indices,
 * contain a change along x (.dx) and y (.dy) axes
 * and know their relatives (.opposite, .left, .right)
 */
public enum Direction {
	
	NORTH(0, -1, 0), EAST(1, 0, 1), SOUTH(2, 1, 0), WEST(3, 0, -1);

	final int nr;
	final int dx;
	final int dy;
	Direction opposite;
	Direction left;
	Direction right;
	
	static {
		NORTH.opposite = SOUTH;
		SOUTH.opposite = NORTH;
		EAST.opposite = WEST;
		WEST.opposite = EAST;
		
		NORTH.left = WEST;
		SOUTH.left = EAST;
		EAST.left = NORTH;
		WEST.left = SOUTH;
		
		SOUTH.right = WEST;
		NORTH.right = EAST;
		WEST.right = NORTH;
		EAST.right = SOUTH;
	}

	private Direction(int nr, int dx, int dy) {
		this.nr = nr;
		this.dx = dx;
		this.dy = dy;
	}
}