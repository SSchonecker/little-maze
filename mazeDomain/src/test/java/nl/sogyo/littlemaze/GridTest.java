package nl.sogyo.littlemaze;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the entire grid.
 */
class GridTest {
	
	private static Grid myGrid;
	private static Grid miniGrid;

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
		assertNotNull(myGrid.getChestLocation());

		int[] expected = {0, 1};
		assertArrayEquals(expected, miniGrid.getChestLocation());
	}
	
	@Test
	void getMazeLayout() {
		char[][][] expected = 	{{{'_', '.', '_', '_'}, {'_', '.', '_', '_'}}, 
							{{'.', '_', '_', '.'},{'_', '_', '.', '.'}}};
		assertArrayEquals(expected, miniGrid.getLayout());
	}
	
	@Test
	void movePlayer() {
		try {
			miniGrid.stirPlayer("w");
		} catch (InvalidMoveException e) {}
		int[] expected = {1,0};
		assertArrayEquals(expected, miniGrid.getPlayerLocation());
		
		try {
			miniGrid.stirPlayer("a");
		} catch (InvalidMoveException e) {}
		assertEquals(Direction.NORTH.toString(), miniGrid.getPlayerOrientation());
		
		assertThrows(InvalidMoveException.class, () -> miniGrid.stirPlayer("s"));
		assertArrayEquals(expected, miniGrid.getPlayerLocation());
		
		try {
			miniGrid.stirPlayer("d");
		} catch (InvalidMoveException e) {}
		assertEquals(Direction.EAST.toString(), miniGrid.getPlayerOrientation());
		
		assertThrows(InvalidMoveException.class, () -> miniGrid.stirPlayer("g"));
	}
	
	@Test
	void tileSelect() {
		try {
			miniGrid.selectTile(1, 0);
		} catch (InvalidMoveException e) {assertFalse(true);}
		
		assertThrows(InvalidMoveException.class, () -> miniGrid.selectTile(0,1));
	}
}
