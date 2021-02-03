package nl.sogyo.littlemaze;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the entire grid.
 */
class GridTest {
	
	private static Grid myGrid;
	private static Grid miniGrid; // Based on the testMaze, so it's always the same

	@BeforeAll
	static void makeGrid() {
		myGrid = new Grid(25);
		myGrid.putPlayer("R");
	}
	
	@BeforeEach
	void makeMiniGrid() {
		miniGrid = new Grid(2);
		miniGrid.putPlayer("Julie");
	}
	
	@Test
	void tooLargeGrid() {
		assertThrows(IndexOutOfBoundsException.class, () -> new Grid(89));
	}

	@Test
	void playerNameInfo() {
		assertEquals("R", myGrid.getPlayerName());
	}
	
	@Test
	void getPlayerScoreAndGameStatus() {
		assertFalse(myGrid.getGameStatus());
		assertEquals(0, myGrid.getScore());
		assertEquals(0, myGrid.getSteps());
		assertEquals(50, myGrid.getPlayerHealth());
	}
	
	@Test
	void getPlayerPosition() {
		int[] expected = {0, 0};
		assertArrayEquals(expected, myGrid.getPlayerLocation());
	}
	
	@Test
	void getChestPosition() {
		assertNotNull(myGrid.getChestLocation()); // Each large grid should have a chest

		int[] expected = {0, 1};
		assertArrayEquals(expected, miniGrid.getChestLocation());
	}
	
	@Test
	void getCorrectMazeLayout() {
		/*
		 * The layout consists of cells 0.0, 0.1, 1.0, 1.1,
		 * each cell should know its type and where the walls _ are
		 */
		char[][][] expected = 	{{{'p','_', '.', '_', '_'}, {'c','_', '.', '_', '_'}}, 
							{{'t','.', '_', '_', '.'},{'s','_', '_', '.', '.'}}};
		assertArrayEquals(expected, miniGrid.getLayout());
	}
	
	@Test
	void movePlayer() {
		try {
			miniGrid.stirPlayer("w");
		} catch (Exception e) { assertFalse(true);}
		int[] expected = {1, 0};
		assertArrayEquals(expected, miniGrid.getPlayerLocation());
		
		try {
			miniGrid.stirPlayer("q");
		} catch (Exception e) { assertFalse(true);}
		assertEquals(Direction.NORTH.toString(), miniGrid.getPlayerOrientation());
		
		expected[0] = 0; // Move back to {0, 0}
		try {
			miniGrid.stirPlayer("a");
		} catch (Exception e) { assertFalse(true);}
		assertArrayEquals(expected, miniGrid.getPlayerLocation());
		
		try {
			miniGrid.stirPlayer("s");
		} catch (Exception e) { assertFalse(true);}
		assertArrayEquals(expected, miniGrid.getPlayerLocation());
		
		try {
			miniGrid.stirPlayer("e");
		} catch (Exception e) { assertFalse(true);}
		assertEquals(Direction.EAST.toString(), miniGrid.getPlayerOrientation());
		
		try {
			miniGrid.stirPlayer("d");
		} catch (Exception e) { assertFalse(true);}
		assertArrayEquals(expected, miniGrid.getPlayerLocation());
		
		assertThrows(Exception.class, () -> miniGrid.stirPlayer("g"));
	}
	
	@Test
	void tileSelect() {
		miniGrid.selectTile(1, 0);
		assertTrue(miniGrid.getTileRevealed(1, 0));
		assertEquals("t", miniGrid.getTileType(1, 0));
		
		assertEquals("c", miniGrid.getTileType(0, 1));
	}
}
