package nl.sogyo.littlemaze;

import java.util.Arrays;

public class Player {

	private Tile currentTile;
	private int myLife = 50;
	private int mySteps = -1;
	private String myName;
	private Direction facing = Direction.EAST;
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

	public void suffer(int pain) {
		System.out.println("Ouch! You lost " + pain + " hp...");
		myLife -= pain;
	}

	public Direction getOrientation() {
		return facing ;
	}

	public void turnLeft() {
		facing = facing.left();
	}

	public void turnRight() {
		facing = facing.right();
	}

	public void putHere(Tile someTile) {
		currentTile = someTile;
		mySteps += 1;
	}

	public void moveForward() {
		int[] temp = Arrays.copyOf(currentTile.getPosition(), 2);
		switch (facing) {
		case NORTH: 
			temp[1] += 1;
			break;
		case EAST: 
			temp[0] += 1;
			break;
		case SOUTH: 
			temp[1] -= 1;
			break;
		case WEST: 
			temp[0] -= 1;
		}
		
		currentTile.walkTo(temp, this);
		
	}

	public void moveBackward() {
		int[] temp = Arrays.copyOf(currentTile.getPosition(), 2);
		switch (facing) {
		case NORTH: 
			temp[1] -= 1;
			break;
		case EAST: 
			temp[0] -= 1;
			break;
		case SOUTH: 
			temp[1] += 1;
			break;
		case WEST: 
			temp[0] += 1;
		}
		
		currentTile.walkTo(temp, this);
		
	}

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

}
