package nl.sogyo.littlemaze.mazeapi.dtostructures;

import nl.sogyo.littlemaze.Grid;

public class PlayerDto {
	
	String name;
	int health;
	int steps;
	int[] position;

	public PlayerDto(Grid mazeGrid, String playerName) {
		this.name = playerName;
		this.health = mazeGrid.getPlayerHealth();
		this.steps = mazeGrid.getSteps();
		
		this.position = mazeGrid.getPlayerLocation();
	}

	public String getName() {
		return name;
	}

	public int getHealth() {
		return health;
	}

	public int getSteps() {
		return steps;
	}

	public int[] getPosition() {
		return position;
	}

}
