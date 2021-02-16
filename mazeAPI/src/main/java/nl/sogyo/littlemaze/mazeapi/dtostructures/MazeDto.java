package nl.sogyo.littlemaze.mazeapi.dtostructures;

import nl.sogyo.littlemaze.Grid;

/**
 * Class to contain the necessary information to OUTPUT a game.
 * Note that for loading a game, the LoadMazeDto should be used.
 * Used in MazeInitiate, MazeSave and MazePlay.
 */
public class MazeDto {
	PlayerDto player;
	GameStatusDto gameStatus;
	char[][][] layout;
	
	public MazeDto() {}
	
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
