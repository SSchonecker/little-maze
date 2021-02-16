package nl.sogyo.littlemaze.mazeapi.dtostructures;

/**
 * Class meant as expansion on MazeDto, deals with loaded json
 * from the DB.
 * Handles the transformation of the layout String matrix
 * into a usable matrix of chars' list.
 * Used in MazeInitiate.
 *
 */
public class LoadMazeDto{
	PlayerDto player;
	GameStatusDto gameStatus;
	String[][] layoutFromJSON;
	
	public PlayerDto getPlayer() {
		return player;
	}

	public void setPlayer(PlayerDto player) {
		this.player = player;
	}

	public GameStatusDto getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(GameStatusDto gameStatus) {
		this.gameStatus = gameStatus;
	}
	
	public char[][][] getLayout() {
		char[][][] layout = new char[layoutFromJSON.length][layoutFromJSON.length][5];
		for (int x = 0; x < layout.length; x++) {
			for (int y = 0; y < layout.length; y++) {
				for (int i = 0; i < 5; i++) {
					layout[x][y][i] = layoutFromJSON[x][y].charAt(i);
				}
			}
		}
		return layout;
	}

	public void setLayout(String[][] layout) {
		layoutFromJSON = layout;
	}

}
