package nl.sogyo.littlemaze.mazeapi.dtostructures;

import nl.sogyo.littlemaze.Grid;

public class MazeDto {
	PlayerDto player;
    public PlayerDto getPlayer() { return player; }
    
    GameStatusDto gameStatus;
    public GameStatusDto getGameStatus() { return gameStatus; }
    
    char[][][] layout;
    public char[][][] getLayout() { return layout; }

    public MazeDto(Grid mazeGrid, String playerName) {
        player = new PlayerDto(mazeGrid, playerName);
        gameStatus = new GameStatusDto(mazeGrid);
        layout = mazeGrid.getLayout();
    }
}
