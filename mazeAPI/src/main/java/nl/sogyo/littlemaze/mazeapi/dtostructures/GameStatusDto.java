package nl.sogyo.littlemaze.mazeapi.dtostructures;

import nl.sogyo.littlemaze.Grid;

public class GameStatusDto {
	
	boolean endgame;
	int score;

	public GameStatusDto(Grid mazeGrid) {
		this.endgame = mazeGrid.getGameStatus();
		this.score = mazeGrid.getScore();
	}

	public boolean getEndgame() {
		return endgame;
	}

	public int getScore() {
		return score;
	}

}
