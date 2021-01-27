package nl.sogyo.littlemaze;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//import java.time.Duration;
//import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests mainly for the separate tiles and the player movement.
 */
class TileTest {
	
	private Tile firstTile;
	private Tile spikedTile;
	private Tile miniMaze;
	private Player aPlayer;
	
	private Tile[][] testMaze = new Tile[2][2];
	
	@BeforeEach
	void setTiles() {
		aPlayer = new Player("R");
		miniMaze = new Tile(testMaze);
		firstTile = miniMaze.getTileAt(0, 0);
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
				miniMaze.getTileAt(0,0).getPosition());
		
		expectedPosition[0] = 1;
		assertArrayEquals(expectedPosition,
				miniMaze.getTileAt(1,0).getPosition());
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
		aPlayer.turnLeft();
		aPlayer.moveForward();
		
		assertTrue(50 > aPlayer.getHealth());
	}
	
	@Test
	void invalidMove() {
		firstTile.moveTo(aPlayer);
		aPlayer.moveForward();
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
		aPlayer.turnLeft();
		aPlayer.moveForward();
		aPlayer.turnLeft();
		aPlayer.moveForward();
		
		int[] expectedPosition = {0, 1};
		
		assertArrayEquals(expectedPosition, aPlayer.getPosition());
		assertEquals(127, aPlayer.getScore());
		assertTrue(aPlayer.isGameOver());
		
		aPlayer.moveBackward();
		assertArrayEquals(expectedPosition, aPlayer.getPosition());
	}
	@Test
	void oneChestOnly() {
		Tile[][] bigMaze = new Tile[10][10];
		@SuppressWarnings("unused")
		Tile firstTile = new Tile(bigMaze);
		int nrOfChests = 0;
		for (Tile[] row : bigMaze) {
			for (Tile aTile : row ) {
				if (aTile.containsChest()) {
					nrOfChests++;
				}
			}
		}
		assertEquals(1, nrOfChests);
	}
	
	/**@Test
	void mazeBuildSpeed() {
		Instant start = Instant.now();
		Tile[][] bigMaze = new Tile[85][85];
		Tile firstTile = new Tile(bigMaze);
		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).toMillis();
		System.out.println("It takes " + timeElapsed + "ms to build a 85x85 maze");
		
		start = Instant.now();
		Tile aTile = firstTile.getTileAt(40, 38);
		finish = Instant.now();
		timeElapsed = Duration.between(start, finish).toMillis();
		System.out.println("It takes " + timeElapsed + "ms to find a specific tile");
	}*/

}
