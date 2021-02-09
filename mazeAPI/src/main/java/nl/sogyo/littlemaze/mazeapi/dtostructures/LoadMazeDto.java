package nl.sogyo.littlemaze.mazeapi.dtostructures;

public class LoadMazeDto{
	PlayerDto player;
	GameStatusDto gameStatus;
	String[][] layoutFromJSON;
	
	public LoadMazeDto() {}
	
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

	public void setLayoutFromJSON(String[][] layoutFromJSON) {
		this.layoutFromJSON = layoutFromJSON;
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
