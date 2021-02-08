package nl.sogyo.littlemaze.mazeapi.dtostructures;

import nl.sogyo.littlemaze.Grid;

public class MazeDto {
	PlayerDto player;
	GameStatusDto gameStatus;
	char[][][] layout;
	
	public MazeDto(Grid mazeGrid, String playerName) {
        player = new PlayerDto(mazeGrid, playerName);
        gameStatus = new GameStatusDto(mazeGrid);
        layout = mazeGrid.getLayout();
    }
	
    public PlayerDto getPlayer() { return player; }
    
    public GameStatusDto getGameStatus() { return gameStatus; }
    
    public char[][][] getLayout() { return layout; }

	public void setPlayer(PlayerDto player) {
		this.player = player;
	}

	public void setGameStatus(GameStatusDto gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void setLayout(char[][][] layout) {
		this.layout = layout;
	}
	
}
