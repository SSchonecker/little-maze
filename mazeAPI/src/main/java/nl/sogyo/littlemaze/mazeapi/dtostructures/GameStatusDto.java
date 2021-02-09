package nl.sogyo.littlemaze.mazeapi.dtostructures;

import nl.sogyo.littlemaze.Grid;

public class GameStatusDto {
	
	boolean endgame;
	int score;
	
	public GameStatusDto() {}

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

	public void setEndgame(boolean endgame) {
		this.endgame = endgame;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
