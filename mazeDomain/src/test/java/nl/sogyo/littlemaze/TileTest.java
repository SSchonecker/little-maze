package nl.sogyo.littlemaze;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TileTest {
	
	private Tile firstTile;
	private Tile spikedTile;
	private Tile miniMaze;
	private Player aPlayer;
	
	private String[] testMazeString = {"O;00;O", "O;10;P", "P;20;O", "O;21;O", "C;22"};
	private List<String> testMaze = new LinkedList<>(Arrays.asList(testMazeString));
	
	@BeforeEach
	void setTiles() {
		aPlayer = new Player("R");
		miniMaze = new Tile(testMaze, null);
		firstTile = miniMaze.getTileNr(0);
		spikedTile = new Spike(2, 5);
	}
	
	@Test
	void getTilePosition() {
		int[] expectedPosition = {0, 0};
		assertArrayEquals(expectedPosition, firstTile.getPosition());
	}
	
	@Test
	void selectTile() {
		firstTile.select();
		assertTrue(firstTile.isChecked());
	}
	
	@Test
	void moveUntoTile() {
		Player aPlayer = new Player("R");
		firstTile.moveTo(aPlayer);
		
		assertEquals(firstTile.getPosition(), aPlayer.getPosition());
		assertEquals(50, aPlayer.getHealth());
	}
	
	@Test
	void moveUntoSpike() {
		spikedTile.moveTo(aPlayer);
		
		assertEquals(spikedTile.getPosition(), aPlayer.getPosition());
		assertTrue(50 > aPlayer.getHealth());
	}
	
	@Test
	void moveUntoSpikeAfterCheck() {
		spikedTile.select();
		spikedTile.moveTo(aPlayer);
		
		assertEquals(spikedTile.getPosition(), aPlayer.getPosition());
		assertEquals(50, aPlayer.getHealth());
	}
	
	@Test
	void makeMiniMaze() {
		int[] expectedPosition = {0, 0};
		assertArrayEquals(expectedPosition,
				miniMaze.getTileNr(0).getPosition());
		assertArrayEquals(expectedPosition,
				miniMaze.getTileAt(0,0).getPosition());
		
		expectedPosition[0] = 2;
		assertArrayEquals(expectedPosition,
				miniMaze.getTileAt(2,0).getPosition());
		assertArrayEquals(expectedPosition,
				miniMaze.getTileNr(2).getPosition());
	}
	
	@Test
	void movePlayer() {
		firstTile.moveTo(aPlayer);
		aPlayer.moveForward();
		
		int[] expectedPosition = {1, 0};
		
		assertArrayEquals(expectedPosition, aPlayer.getPosition());
	}
	
	@Test
	void spikePlayer() {
		firstTile.moveTo(aPlayer);
		aPlayer.moveForward();
		aPlayer.moveForward();
		
		assertTrue(50 > aPlayer.getHealth());
	}
	
	@Test
	void invalidMove() {
		firstTile.moveTo(aPlayer);
		aPlayer.moveForward();
		aPlayer.turnLeft();
		aPlayer.moveForward();
		
		int[] expectedPosition = {1, 0};
		
		assertArrayEquals(expectedPosition, aPlayer.getPosition());
	}
	
	@Test
	void moveBack() {
		firstTile.moveTo(aPlayer);
		aPlayer.moveForward();
		aPlayer.moveBackward();
		
		int[] expectedPosition = {0, 0};
		
		assertArrayEquals(expectedPosition, aPlayer.getPosition());
	}
	
	@Test
	void invalidMoveBack() {
		firstTile.moveTo(aPlayer);
		aPlayer.moveBackward();
		
		int[] expectedPosition = {0, 0};
		
		assertArrayEquals(expectedPosition, aPlayer.getPosition());
	}
	
	@Test
	void walkOnChest() {
		firstTile.moveTo(aPlayer);
		aPlayer.moveForward();
		aPlayer.moveForward();
		aPlayer.turnLeft();
		aPlayer.moveForward();
		aPlayer.moveForward();
		
		int[] expectedPosition = {2, 2};
		
		assertArrayEquals(expectedPosition, aPlayer.getPosition());
		assertEquals(126, aPlayer.getScore());
		assertTrue(aPlayer.isGameOver());
	}

}
