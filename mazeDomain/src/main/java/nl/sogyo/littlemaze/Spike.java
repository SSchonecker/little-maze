package nl.sogyo.littlemaze;

/**
 * Particular type of tile, that will hurt the player when stepped on blindly
 */
public class Spike extends Tile {

	private boolean revealed = false;
	private final int PAIN = 20; // The health penalty to the player

	public Spike(int x, int y) {
		super(x, y);
	}
	
	public Spike (Tile[][] mazeGrid, Direction fromLast, Tile last) {
		super(mazeGrid, fromLast, last);
	}
	
	/**
	 * Method to get the spike type
	 * @return returns either "s" for spike or "h" for harmless,
	 * i.e. if the spikes are revealed
	 */
	@Override
	public String type() {
		if (revealed) {
			return "h";
		}
		return "s";
	}
	
	@Override
	public void select() { // This seems to be necessary to work with the Spike class
		revealed = true;
	}
	
	/**
	 * Method to put the player on this tile.
	 * Causes the player to suffer if he blindly steps onto this spikey tile
	 * 
	 * @param aPlayer The player to be put on this tile
	 */
	@Override
	public void moveTo(Player aPlayer) {
		aPlayer.putHere(this);
		if (!revealed) {
			aPlayer.suffer(PAIN);
		}
	}

}
