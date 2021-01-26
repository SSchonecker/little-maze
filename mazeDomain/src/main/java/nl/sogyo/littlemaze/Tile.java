package nl.sogyo.littlemaze;

import java.util.Arrays;
import java.util.List;

class Tile {
	
	private int[] position = new int[2];
	private boolean revealed = false;
	Tile nextTile;
	Tile prevTile;
	private boolean hasChest = false;
	private final int TREASURE = 100;

	public Tile(int x, int y) {
		position[0] = x;
		position[1] = y;
	}

	public Tile(List<String> mazeList, Tile previousTile) {
		prevTile = previousTile;
		
		String info = mazeList.remove(0);
		String positionInfo = info.split(";")[1];
		position[0] = Character.getNumericValue(positionInfo.charAt(0));
		position[1] = Character.getNumericValue(positionInfo.charAt(1));
		
		if (info.split(";")[0].equals("C")) {
			hasChest = true;
			prevTile = null;
		}
		else {
			String nextTileInfo = info.split(";")[2];
			if (nextTileInfo.equals("O")) {
				nextTile = new Tile(mazeList, this);
			}
			else if (nextTileInfo.equals("P")) {
				String nextInfo = mazeList.remove(0);
				String nextPositionInfo = nextInfo.split(";")[1];
				nextTile = new Spike(Character.getNumericValue(nextPositionInfo.charAt(0)),
						Character.getNumericValue(nextPositionInfo.charAt(1)),
						nextInfo, mazeList, this);
			}
		}
	}

	public int[] getPosition() {
		return position;
	}

	public void select() {
		if (hasChest) {
			System.out.println("You found the chest!");
		}
		else {
			System.out.println("This looks like an ordinary tile.");
		}
		revealed = true;
	}

	public boolean isChecked() {
		return revealed;
	}

	public void moveTo(Player aPlayer) {
		aPlayer.putHere(this);
		if (hasChest) {
			aPlayer.setScore(TREASURE);
		}
	}

	public Tile getTileAt(int i, int j) {
		if (position[0] == i && position[1] == j) {
			return this;
		}
		return nextTile.getTileAt(i, j);
	}

	public Tile getTileNr(int n) {
		if (n == 0) {
			return this;
		}
		return nextTile.getTileNr(n-1);
	}

	public void walkTo(int[] temp, Player aPlayer) {
		if (nextTile != null && Arrays.equals(nextTile.getPosition(), temp)) {
			nextTile.moveTo(aPlayer);
			return;
		}
		else if (prevTile != null && Arrays.equals(prevTile.getPosition(), temp)) {
			prevTile.moveTo(aPlayer);
			return;
		}
		else {
			System.out.println("You can't move there.");
		}
	}

}
