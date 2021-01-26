package nl.sogyo.littlemaze;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TileTest {
	
	@Test
	public void getTilePosition() {
		Tile firstTile = new Tile(2, 4);
		int[] expectedPosition = {2,4};
		
		assertArrayEquals(expectedPosition, firstTile.getPosition());
	}
	
	@Test
	public void selectTile() {
		Tile firstTile = new Tile(2, 4);
		firstTile.select();
		
		assertTrue(firstTile.isChecked());
	}

}
