package nl.sogyo.littlemaze;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.sogyo.littlemaze.Direction;
import nl.sogyo.littlemaze.Player;
import nl.sogyo.littlemaze.Tile;

/**
 * Unit tests for the basic player functions.
 */
class PlayerTest {
	
	Player thePlayer;
	
	@BeforeEach
	private void makePlayer() {
		thePlayer = new Player("R");
	}
	
	@Test
	void getPlayerPosition() {
		Tile playersTile = new Tile(0,0);
		playersTile.moveTo(thePlayer);
		
		int[] expectedPosition = {0,0};
		
		assertArrayEquals(expectedPosition, thePlayer.getPosition());
	}
	
	@Test
	void getPlayerName() {
		assertEquals("R", thePlayer.getName());
	}
	
	@Test
	void getPlayerHealth() {
		assertEquals(50, thePlayer.getHealth());
	}
	
	@Test
	void hurtPlayer() {
		thePlayer.suffer(4);
		assertEquals(46, thePlayer.getHealth());
	}
	
	@Test
	void killPlayer() {
		thePlayer.suffer(52);
		assertEquals(0, thePlayer.getHealth());
		assertTrue(thePlayer.isGameOver());
	}
	
	@Test
	void letPlayerTurn() {
		thePlayer.turnLeft();
		assertEquals(Direction.NORTH, thePlayer.getOrientation());
		
		thePlayer.turnRight();
		thePlayer.turnRight();
		thePlayer.turnRight();
		assertEquals(Direction.WEST, thePlayer.getOrientation());
	}
}
