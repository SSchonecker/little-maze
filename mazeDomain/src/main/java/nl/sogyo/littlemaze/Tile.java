package nl.sogyo.littlemaze;

class Tile {
	
	int[] position = new int[2];
	boolean revealed = false;

	public Tile(int x, int y) {
		position[0] = x;
		position[1] = y;
	}

	public int[] getPosition() {
		return position;
	}

	public void select() {
		revealed = true;
	}

	public boolean isChecked() {
		return revealed;
	}

}
