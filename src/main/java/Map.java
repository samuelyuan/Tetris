public class Map {
	private int[][] map;
	private int numRows, numCols;

	public Map(int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
		map = new int[numRows][numCols];

		clear();
	}

	public int getTile(int r, int c) {
		if (isValid(r, c)) {
			return map[r][c];
		} else {
			return -10000;
		}
	}

	public void setTile(int r, int c, int tile) {
		if (isValid(r, c)) {
			map[r][c] = tile;
		}
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public boolean isValid(int r, int c) {
		if (r < 0 || c < 0 || r >= numRows || c >= numCols) {
			return false;
		} else {
			return true;
		}
	}

	public void clear() {
		for (int r = 0; r < numRows; r++) {
			clearRow(r);
		}
	}

	public void clearRow(int rowNum) {
		for (int c = 0; c < numCols; c++) {
			map[rowNum][c] = 0;
		}
	}

	public boolean isLineFilled(int rowNum) {
		for (int c = 0; c < getNumCols(); c++) {
			// line isn't filled, since there's an empty space
			if (getTile(rowNum, c) == 0) {
				return false;
			}
		}

		return true;
	}

	public void shiftRowDown(int rowNum) {
		// carry everything else down to the ground
		for (int startingY = rowNum - 1; startingY >= 0; startingY--) {
			for (int x = 0; x < getNumCols(); x++) {
				setTile(startingY + 1, x, getTile(startingY, x));
			}
		}
	}
}
