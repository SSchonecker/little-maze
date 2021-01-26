package nl.sogyo.littlemaze;

class Tile {
	
	private int[] position = new int[2];
	private boolean revealed = false;

	public Tile(int x, int y) {
		position[0] = x;
		position[1] = y;
	}

	public int[] getPosition() {
		return position;
	}

	public void select() {
		System.out.println("This looks like an ordinary tile.");
		revealed = true;
	}

	public boolean isChecked() {
		return revealed;
	}

	public void moveTo(Player aPlayer) {
		aPlayer.putHere(position);
		
	}

}
