package nl.sogyo.littlemaze;

public class Spike extends Tile {

	private boolean revealed = false;
	private static final int PAIN = 20;

	public Spike(int x, int y) {
		super(x, y);
	}
	
	public Spike (Tile[][] mazeGrid, Direction fromLast, Tile last) {
		super(mazeGrid, fromLast, last);
	}
	
	@Override
	public void select() {
		System.out.println("Careful now! This looks like a spikey tile.");
		revealed  = true;
	}
	
	@Override
	public void moveTo(Player aPlayer) {
		aPlayer.putHere(this);
		if (!revealed) {
			System.out.println("This is a spikey tile!");
			aPlayer.suffer(PAIN);
		}
	}

}
