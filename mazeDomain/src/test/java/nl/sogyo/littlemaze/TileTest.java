package nl.sogyo.littlemaze;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests mainly for the separate tiles and the player movement.
 */
class TileTest {
	
	private Tile firstTile;
	private Tile spikedTile;
	private Player aPlayer;
	
	private Tile[][] testMaze = new Tile[2][2]; // Because of the first neighbour being build to the south, this is a fixed maze
	
	@BeforeEach
	void setTiles() {
		aPlayer = new Player("R");
		firstTile = new Tile(testMaze);
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
	void correctTileType() {
		assertEquals("t", firstTile.type());
		assertEquals("s", spikedTile.type());
		spikedTile.select();
		assertEquals("h", spikedTile.type());
		assertEquals("c", firstTile.getTileAt(0,1).type());
	}
	
	@Test
	void moveUntoTile() {
		Player aPlayer = new Player("R");
		firstTile.moveTo(aPlayer);
		
		assertEquals(firstTile.getPosition(), aPlayer.getPosition());
		assertEquals(100, aPlayer.getHealth());
	}
	
	@Test
	void moveUntoSpike() {
		spikedTile.moveTo(aPlayer);
		
		assertEquals(spikedTile.getPosition(), aPlayer.getPosition());
		assertTrue(100 > aPlayer.getHealth());
	}
	
	@Test
	void moveUntoSpikeAfterCheck() {
		spikedTile.select();
		spikedTile.moveTo(aPlayer);
		
		assertEquals(spikedTile.getPosition(), aPlayer.getPosition());
		assertEquals(100, aPlayer.getHealth());
	}
	
	@Test
	void makeTestMaze() {
		int[] expectedPosition = {0, 0};
		assertArrayEquals(expectedPosition,
				firstTile.getPosition());
		
		expectedPosition[0] = 1;
		assertArrayEquals(expectedPosition,
				firstTile.getTileAt(1,0).getPosition());
	}
	
	@Test
	void tryGetWrongTile() {
		assertNull(firstTile.getTileAt(2,4));
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
		aPlayer.moveForward(); // Here, the player moved unto a spikey tile
		
		assertTrue(100 > aPlayer.getHealth());
	}
	
	@Test
	void invalidMove() {
		firstTile.moveTo(aPlayer);
		aPlayer.moveForward();
		aPlayer.moveForward(); // The player can't make this step and should stay put
		
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
	void moveLeftAndRight() {
		firstTile.moveTo(aPlayer);
		aPlayer.moveDownward();
		aPlayer.moveRight();
		int[] expectedPosition = {1, 1};
		assertArrayEquals(expectedPosition, aPlayer.getPosition());

		aPlayer.moveLeft();
		
		expectedPosition[1] = 0;
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
		firstTile.getTileAt(aPlayer.getPosition()).getNeighbour(3).select();
		aPlayer.moveForward();
		
		int[] expectedPosition = {0, 1};
		
		assertArrayEquals(expectedPosition, aPlayer.getPosition());
		assertEquals(97, aPlayer.getScore());
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
		Tile chestTile = null;
		for (Tile[] row : bigMaze) {
			for (Tile aTile : row ) {
				if (aTile.containsChest()) {
					nrOfChests++;
					chestTile = aTile;
				}
			}
		}
		assertEquals(1, nrOfChests);
		assertNotNull(chestTile);
		assertEquals(500, chestTile.getContent());
	}
}
