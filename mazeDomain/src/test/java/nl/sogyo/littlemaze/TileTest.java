package nl.sogyo.littlemaze;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TileTest {
	
	private Tile firstTile;
	private Tile spikedTile;
	private Tile miniMaze;
	
	private String[] testMaze = {"O;00;E", "O;10;E", "P;20;S", "O;21;S", "C;22;F"};
	
	@BeforeEach
	void setTiles() {
		firstTile = new Tile(2, 4);
		spikedTile = new Spike(2, 5);
		//miniMaze = new Tile(testMaze);
	}
	
	@Test
	void getTilePosition() {
		int[] expectedPosition = {2,4};
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
		Player aPlayer = new Player("R");
		spikedTile.moveTo(aPlayer);
		
		assertEquals(spikedTile.getPosition(), aPlayer.getPosition());
		assertTrue(50 > aPlayer.getHealth());
	}
	
	@Test
	void moveUntoSpikeAfterCheck() {
		Player aPlayer = new Player("R");
		spikedTile.select();
		spikedTile.moveTo(aPlayer);
		
		assertEquals(spikedTile.getPosition(), aPlayer.getPosition());
		assertEquals(50, aPlayer.getHealth());
	}
	
	@Test
	void makeMiniMaze() {
		
	}

}
