package nl.sogyo.littlemaze;

public enum Direction {
	NORTH, EAST, SOUTH, WEST;

	Direction left() {
		switch (this) {
			case NORTH: 
				return Direction.WEST;
			case EAST: 
				return Direction.NORTH;
			case SOUTH: 
				return Direction.EAST;
			case WEST: 
				return Direction.SOUTH;
		}
		return null;
	}

	Direction right() {
		switch (this) {
			case NORTH: 
				return Direction.EAST;
			case EAST: 
				return Direction.SOUTH;
			case SOUTH: 
				return Direction.WEST;
			case WEST: 
				return Direction.NORTH;
		}
		return null;
	}
}
