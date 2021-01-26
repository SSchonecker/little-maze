package nl.sogyo.littlemaze;

public class Spike extends Tile {

	private boolean revealed = false;
	private int pain = 2;

	public Spike(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void select() {
		System.out.println("Careful now! This looks like a spikey tile.");
		revealed  = true;
	}
	
	@Override
	public void moveTo(Player aPlayer) {
		aPlayer.putHere(this.getPosition());
		if (!revealed) {
			System.out.println("This is a spikey tile!");
			aPlayer.suffer(pain);
		}
	}

}
