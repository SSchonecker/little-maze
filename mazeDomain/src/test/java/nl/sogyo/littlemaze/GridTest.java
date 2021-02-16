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
		assertEquals(100, myGrid.getPlayerHealth());
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
		char[][][] expected = 	{{{'p','_', '_', '.', '_'}, {'c','_', '_', '.', '_'}}, 
							{{'t','.', '.', '_', '_'},{'s','.', '_', '_', '.'}}};
		assertArrayEquals(expected, miniGrid.getLayout());
	}
	
	@Test
	void getCorrectMazeLayoutOnSpikeSelect() {
		/*
		 * The layout consists of cells 0.0, 0.1, 1.0, 1.1,
		 * each cell should know its type and where the walls _ are
		 */
		try {
			miniGrid.stirPlayer("s"); // Moving south
		} catch (Exception e) { assertFalse(true);}
		miniGrid.selectTile(1, 1);
		char[][][] expected = {{{'t','_', '_', '.', '_'}, {'c','_', '_', '.', '_'}}, 
							{{'p','.', '.', '_', '_'},{'h','.', '_', '_', '.'}}};
		assertArrayEquals(expected, miniGrid.getLayout());
	}
	
	@Test
	void movePlayer() {
		try {
			miniGrid.stirPlayer("s"); // Moving south
		} catch (Exception e) { assertFalse(true);}
		int[] expected = {1, 0};
		assertArrayEquals(expected, miniGrid.getPlayerLocation());
		
		try {
			miniGrid.stirPlayer("q"); // Turning left from south to east
		} catch (Exception e) { assertFalse(true);}
		assertEquals(Direction.EAST.toString(), miniGrid.getPlayerOrientation());
		
		expected[0] = 0; // Move north, back to {0, 0}
		try {
			miniGrid.stirPlayer("w");
		} catch (Exception e) { assertFalse(true);}
		assertArrayEquals(expected, miniGrid.getPlayerLocation());
		
		try {
			miniGrid.stirPlayer("s"); // Move south again
		} catch (Exception e) { assertFalse(true);}
		
		try {
			miniGrid.stirPlayer("e"); // Turn right, from east to south
		} catch (Exception e) { assertFalse(true);}
		assertEquals(Direction.SOUTH.toString(), miniGrid.getPlayerOrientation());
		
		expected[0] = 1;
		expected[1] = 1;
		try {
			miniGrid.stirPlayer("d"); // Move to the east
		} catch (Exception e) { assertFalse(true);}
		assertArrayEquals(expected, miniGrid.getPlayerLocation());
		
		expected[1] = 0;
		try {
			miniGrid.stirPlayer("a"); // Move to the west
		} catch (Exception e) { assertFalse(true);}
		assertArrayEquals(expected, miniGrid.getPlayerLocation());
		
		assertThrows(Exception.class, () -> miniGrid.stirPlayer("g"));
	}
	
	@Test
	void tileSelect() {
		assertTrue(miniGrid.selectTile(1, 0));
		assertTrue(miniGrid.getTileRevealed(1, 0));
		assertFalse(miniGrid.selectTile(1, 1));
		assertTrue(miniGrid.selectTile(0, 0));
		
		assertEquals("t", miniGrid.getTileType(1, 0));
		assertEquals("c", miniGrid.getTileType(0, 1));
	}
	
	@Test
	void buildGridFromLayout() {
		char[][][] initialLayout = {{{'t','_', '_', '.', '_'}, {'c','_', '_', '.', '_'}}, 
				{{'p','.', '.', '_', '_'},{'h','.', '_', '_', '.'}}};
		Grid thisGrid = new Grid("R", 10, 10, initialLayout);
		
		char[][][] expectedLayout = {{{'t','_', '_', '.', '_'}, {'c','_', '_', '.', '_'}}, 
				{{'p','.', '.', '_', '_'},{'h','.', '_', '_', '.'}}};		
		assertArrayEquals(expectedLayout, thisGrid.getLayout());
	}
	
	@Test
	void correctChestContents() {
		// Check for the basic newly generated maze and for the rebuild one
		assertEquals(20, miniGrid.getChestContent());
		
		char[][][] initialLayout = {{{'t','_', '_', '.', '_'}, {'c','_', '_', '.', '_'}}, 
				{{'p','.', '.', '_', '_'},{'h','.', '_', '_', '.'}}};
		Grid thisGrid = new Grid("R", 10, 10, initialLayout);
		assertEquals(20, thisGrid.getChestContent());
	}
}
