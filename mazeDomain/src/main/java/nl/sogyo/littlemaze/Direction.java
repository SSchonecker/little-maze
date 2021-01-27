package nl.sogyo.littlemaze;

public enum Direction {
	NORTH(0, 0, 1), EAST(1, 1, 0), SOUTH(2, 0, -1), WEST(3, -1, 0);

	final int nr;
	final int dx;
	final int dy;
	Direction opposite;
	Direction left;
	Direction right;

	// use the static initializer to resolve forward references
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