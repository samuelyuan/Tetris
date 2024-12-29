import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TetrisTest {

	@Test
	public void testIsLineFilled() {
		Map board = new Map(3, 4);

		// fill up the first row
		for (int i = 0; i < board.getNumCols(); i++) {
			board.setTile(2, i, 1);
		}

		// put a few tiles on the second row
		for (int i = 0; i < board.getNumCols() - 2; i++) {
			board.setTile(1, i, 1);
		}

		// first row is filled
		assertTrue(board.isLineFilled(2));
		// second row isn't filled
		assertFalse(board.isLineFilled(1));
	}

	@Test
	public void testShiftRowDown() {
		Map board = new Map(3, 4);

		// fill up the first row
		for (int i = 0; i < board.getNumCols(); i++) {
			board.setTile(2, i, 1);
		}

		// put 2 tiles on the second row
		for (int i = 0; i < 2; i++) {
			board.setTile(1, i, 1);
		}

		// put 1 tile on the third row
		for (int i = 0; i < 1; i++) {
			board.setTile(0, i, 1);
		}

		// clear first row
		board.clearRow(2);

		// shift down the second and third rows
		board.shiftRowDown(2);

		// first row should have 2 tiles
		assertEquals(board.getTile(2, 0), 1);
		assertEquals(board.getTile(2, 1), 1);
		assertEquals(board.getTile(2, 2), 0);
		assertEquals(board.getTile(2, 3), 0);

		// second row should have 1 tile
		assertEquals(board.getTile(1, 0), 1);
		assertEquals(board.getTile(1, 1), 0);
		assertEquals(board.getTile(1, 2), 0);
		assertEquals(board.getTile(1, 3), 0);
	}
}
