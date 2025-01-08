import java.util.Arrays;

public class Tetris {
	private Map board;
	private Block currentBlock, nextBlock;
	public static final int NUM_BLOCKS = 7;
	private boolean isInit = false;
	private boolean isMoving = true;

	private int linesCleared = 0;
	private int numLevels = 1;
	private boolean isGameOver = false;
	private int score = 0;

	private int timeUntilNextUpdate = 400;

	public Map getBoard() {
		return board;
	}

	public Block getCurrentBlock() {
		return currentBlock;
	}

	public Block getNextBlock() {
		return nextBlock;
	}

	public boolean getIsInit() {
		return isInit;
	}

	public boolean getIsMoving() {
		return isMoving;
	}

	public boolean getIsGameOver() {
		return isGameOver;
	}

	public int getScore() {
		return score;
	}

	public int getLinesCleared() {
		return linesCleared;
	}

	public int getNumLevels() {
		return numLevels;
	}

	public int getTimeUntilNextUpdate() {
		return timeUntilNextUpdate;
	}

	public Tetris(int numRows, int numCols) {
		board = new Map(numRows, numCols);
		nextBlock = createNewBlock();
	}

	public void resetGame() {
		board.clear();
		currentBlock = null;
		nextBlock = createNewBlock();
		isInit = false;
		isMoving = true;
		isGameOver = false;
		score = 0;
		linesCleared = 0;
		numLevels = 1;
		timeUntilNextUpdate = 400;
	}

	public Block createNewBlock() {
		int randNum = (int) (Math.random() * Tetris.NUM_BLOCKS) + 1;
		return new Block(randNum);
	}

	public boolean isOkToMove(int r, int c) {
		return (board.isValid(r, c) && (board.getTile(r, c) == 0));
	}

	public void moveBlock(int shiftR, int shiftC) {
		if (currentBlock == null) {
			return;
		}

		// test each tile in the block for collisions or out of bounds
		for (Vector2D tile : currentBlock.getTileArray()) {
			int c = (int) tile.getX();
			int r = (int) tile.getY();

			if (!isOkToMove(r + shiftR, c + shiftC)) {
				return;
			}
		}

		currentBlock.shiftTiles(shiftR, shiftC);
	}

	public void rotateBlock() {
		if (currentBlock == null) {
			return;
		}

		// rotate around center
		currentBlock.rotateTiles(currentBlock.getTile(1));

		// check if the block is out of bounds
		boolean outOfBounds = true;
		int shiftDir = 0;
		while (outOfBounds == true) {
			// check if any one of the tiles is out of bounds
			outOfBounds = false;
			for (int i = 0; i < 4; i++) {
				// the tile is past the left side of the screen
				if (currentBlock.getTile(i).getX() < 0) {
					// shift right
					outOfBounds = true;
					shiftDir = 1;
					break;
				}
				// the tile is past the right side of the screen
				else if (currentBlock.getTile(i).getX() >= board.getNumCols()) {
					// shift left
					outOfBounds = true;
					shiftDir = -1;
					break;
				}
			}

			// shift the block back to the board
			if (outOfBounds == true) {
				currentBlock.shiftTiles(0, shiftDir);
			}
		}
	}

	public void mainLoop() {
		if (!isGameOver) {
			updateBlocks();
			clearLine();
		}
	}

	public boolean canBlockMoveDown(Block currentBlock) {
		// check if the block can still move down
		return Arrays.stream(currentBlock.getTileArray())
			.allMatch(tile -> isOkToMove((int) tile.getY() + 1, (int) tile.getX()));
	}

	public void updateBlocks() {
		if (!isInit) {
			currentBlock = nextBlock;
			nextBlock = createNewBlock();

			isInit = true;
			isMoving = true;

			// update blocks in next cycle
			return;
		}

		isMoving = canBlockMoveDown(currentBlock);
		if (isMoving) {
			currentBlock.shiftTiles(1, 0); // move down
		} else {
			if (!placeNewBlock(currentBlock)) {
				isGameOver = true;
			}

			// set init to false, so new block can be made
			isInit = false;

			score += 10;
		}

	}

	public boolean placeNewBlock(Block newBlock) {
		for (Vector2D tile : newBlock.getTileArray()) {
			int newC = (int) tile.getX();
			int newR = (int) tile.getY();
			board.setTile(newR, newC, newBlock.getTypeOfBlock());

			// existing blocks piled up
			// new block can't be placed
			if (newR <= 1) {
				return false;
			}
		}

		// new block was placed successfully
		return true;
	}

	public void clearLine() {
		// don't bother checking if the block is still moving
		if (isMoving) {
			return;
		}

		// check from bottom to top
		for (int r = board.getNumRows() - 1; r >= 0; r--) {
			// check each tile in a row
			if (board.isLineFilled(r)) {
				board.clearRow(r);

				// the row of blocks above falls
				// check this row again to determine whether it needs to be cleared again
				board.shiftRowDown(r);
				r++;

				updateGameScore();
			}
		}
	}

	public void updateGameScore() {
		linesCleared++;

		score += 40 * (numLevels + 1);

		if (linesCleared % 10 == 0) {
			// increase game speed
			if (timeUntilNextUpdate > 100) {
				timeUntilNextUpdate -= 25;
			} else {
				if (timeUntilNextUpdate > 50) {
					timeUntilNextUpdate -= 10;
				}
			}
			numLevels++;
		}
	}
}
