package nl.sogyo.littlemaze;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
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
		miniGrid = new Grid(2);
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
}
