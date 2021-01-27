package nl.sogyo.littlemaze;

import java.util.Arrays;

public class Grid {
	
	private Tile firstTile;
	private Tile[][] theMaze;
	private Player myPlayer;

	public Grid(int size) {
		if (size > 85) {
			throw new IndexOutOfBoundsException();
		}
		theMaze = new Tile[size][size];
		firstTile = new Tile(theMaze);
	}
	
	public void putPlayer(String playerName) {
		myPlayer = new Player(playerName);
		firstTile.moveTo(myPlayer);
	}

	public String getPlayerName() {
		if (myPlayer != null) {
			return myPlayer.getName();
		}
		return null;
	}

	public boolean getGameStatus() {
		return myPlayer.isGameOver();
	}

	public int getScore() {
		return myPlayer.getScore();
	}

	public int[] getPlayerLocation() {
		return myPlayer.getPosition();
	}

	public int[] getChestLocation() {
		for (Tile[] row : theMaze) {
			for (Tile aTile : row ) {
				if (aTile.containsChest()) {
					return aTile.getPosition();
				}
			}
		}
		return null;
	}

	public char[][][] getLayout() {
		char[][][] mazeLayout = new char[theMaze.length][theMaze.length][4];
		for (int x = 0; x < theMaze.length; x++) {
			for (int y = 0; y < theMaze.length; y++) {
				Tile cell = theMaze[x][y];
				for (int n = 0; n < 4; n++) {
					if(cell.getNeighbour(n) == null) {
						mazeLayout[x][y][n] = '_';
					}
					else {
						mazeLayout[x][y][n] = '.';
					}
				}
			}
		}
		
		return mazeLayout;
	}

	public int getSteps() {
		return myPlayer.getSteps();
	}

	public Integer getPlayerHealth() {
		return myPlayer.getHealth();
	}

	public void stirPlayer(String key) throws InvalidMoveException {
		int[] initPosition = myPlayer.getPosition();
		Direction initFacing = myPlayer.getOrientation();
		switch (key.toLowerCase()) {
		case("w"):
			myPlayer.moveForward();
			break;
		case("s"):
			myPlayer.moveBackward();
			break;
		case("a"):
			myPlayer.turnLeft();
			break;
		case("d"):
			myPlayer.turnRight();
			break;
		default:
			throw new InvalidMoveException("Not a valid key");
		}
		
		if (myPlayer.getPosition() == initPosition &&
				myPlayer.getOrientation().equals(initFacing)) {
			throw new InvalidMoveException("You can't move there.");
		}
	}

	public String getPlayerOrientation() {
		return myPlayer.getOrientation().toString();
	}

	public void selectTile(int i, int j) throws InvalidMoveException {
		int[] target = {i, j};
		for (int n = 0; n < 4; n++) {
			Tile aNeighbour = firstTile.getTileAt(myPlayer.getPosition()).getNeighbour(n);
			if (aNeighbour != null 
					&& Arrays.equals(target, aNeighbour.getPosition())) {
				aNeighbour.select();
				return;
			}
		}
		throw new InvalidMoveException("You can't see this tile from here.");
	}
	
	
}
