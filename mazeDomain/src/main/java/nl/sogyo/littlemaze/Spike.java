package nl.sogyo.littlemaze;

import java.util.List;

public class Spike extends Tile {

	private boolean revealed = false;
	private final int PAIN = 20;

	public Spike(int x, int y) {
		super(x, y);
	}
	
	public Spike(int x, int y, String info, List<String> mazeList, Tile prevTile) {
		super(x, y);
		this.prevTile = prevTile;
		
		String nextTileInfo = info.split(";")[2];
		if (nextTileInfo.equals("O")) {
			nextTile = new Tile(mazeList, this);
		}
		else if (nextTileInfo.equals("P")) {
			String nextInfo = mazeList.remove(0);
			String positionInfo = nextInfo.split(";")[1];
			nextTile = new Spike(Character.getNumericValue(positionInfo.charAt(0)),
					Character.getNumericValue(positionInfo.charAt(1)),
					nextInfo, mazeList, this);
		}
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
