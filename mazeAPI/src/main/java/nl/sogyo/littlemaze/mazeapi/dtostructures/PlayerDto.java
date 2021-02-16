package nl.sogyo.littlemaze.mazeapi.dtostructures;

import nl.sogyo.littlemaze.Grid;

/**
 * Class containing the elementary information on a Player object
 * from the domain.
 * Used as a sub-class in MazeDto and LoadMazeDto
 */
public class PlayerDto {
	String name;
	int health;
	int steps;
	int[] position;
	
	public PlayerDto() {}

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

	public void setName(String name) {
		this.name = name;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

}
