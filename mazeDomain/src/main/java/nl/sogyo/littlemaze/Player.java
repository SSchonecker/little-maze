package nl.sogyo.littlemaze;

public class Player {

	private int[] position = {0,0};
	private int myLife = 50;
	private String myName;
	private Direction facing = Direction.EAST;
	
	public Player(String name) {
		myName = name;
	}

	public int[] getPosition() {
		return position;
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

	public void putHere(int[] position) {
		this.position = position;
	}

}
