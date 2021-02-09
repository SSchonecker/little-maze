package nl.sogyo.littlemaze;

public class Player {

	private Tile currentTile;
	private int myLife = 50;
	private int mySteps = -1; // Initiate at -1, because the first tile will also add 1
	private String myName;
	private Direction facing = Direction.SOUTH; // Same direction as the first tile's neighbour
	private boolean endOfGame = false;
	private int score;
	
	public Player(String name) {
		myName = name;
	}

	public int[] getPosition() {
		return currentTile.getPosition();
	}

	public String getName() {
		return myName;
	}

	public int getHealth() {
		return myLife;
	}
	
	public void setHealth(int playerHealth) {
		myLife = playerHealth;
	}

	public void suffer(int pain) {
		if (pain < myLife) {
			myLife -= pain;
		}
		else {
			myLife = 0;
			score = -mySteps;
			endOfGame = true;
		}
	}

	public Direction getOrientation() {
		return facing ;
	}

	public void turnLeft() {
		facing = facing.left;
	}

	public void turnRight() {
		facing = facing.right;
	}

	/**
	 * Method for putting the player on some tile. Increases step count.
	 * 
	 * @param someTile the tile unto which the player is put
	 */
	public void putHere(Tile someTile) {
		currentTile = someTile;
		mySteps += 1;
	}

	public void moveForward() {
		currentTile.walkTo(facing, this);
	}

	public void moveBackward() {
		currentTile.walkTo(facing.opposite, this);
	}

	/**
	 * Method for finishing the game and setting the score, once the chest is found. 
	 * 
	 * @param chestContent The amount of treasure found in the chest
	 */
	public void setScore(int chestContent) {
		score = chestContent + myLife - mySteps;
		endOfGame = true;
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean isGameOver() {
		return endOfGame;
	}

	public int getSteps() {
		return mySteps;
	}
	
	public void setSteps(int playerSteps) {
		mySteps = playerSteps;
	}

	public void moveLeft() {
		currentTile.walkTo(Direction.WEST, this);		
	}

	public void moveRight() {
		currentTile.walkTo(Direction.EAST, this);		
	}

	public void moveUpward() {
		currentTile.walkTo(Direction.NORTH, this);
	}

	public void moveDownward() {
		currentTile.walkTo(Direction.SOUTH, this);
	}

}
