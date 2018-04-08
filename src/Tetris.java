public class Tetris 
{
	private Map board;
	private Block newBlock, nextBlock;
	public static final int NUM_BLOCKS = 7;
	private boolean isInit = false;
	private boolean isMoving  = true;

	private int linesCleared = 0;
	private int numLevels = 1;
	private boolean isGameOver = false;
	private int score = 0;
	
	private int timeUntilNextUpdate = 400;
	
	public Map getBoard() { return board; }
	public Block getNewBlock() { return newBlock; }
	public void setNewBlock(Block newBlock) { this.newBlock = newBlock; }
	public Block getNextBlock() { return nextBlock; }
	
	public boolean getIsInit() { return isInit; }
	public void setIsInit(boolean isInit) { this.isInit = isInit; }
	public boolean getIsMoving() { return isMoving; }
	public void setIsMoving(boolean isMoving) { this.isMoving = isMoving; } 
	public boolean getIsGameOver() { return isGameOver; }
	
	public int getScore() { return score; }
	public int getLinesCleared() { return linesCleared; }
	public int getNumLevels() { return numLevels; }
	public int getTimeUntilNextUpdate() { return timeUntilNextUpdate; } 
	
	public Tetris ( int numRows, int numCols )
	{
		board = new Map (numRows, numCols);
		int randNum = (int)(Math.random() * Tetris.NUM_BLOCKS) + 1;
		nextBlock = new Block( randNum ) ;
	}
	
	public boolean isOkToMove( int r, int c )
	{
		return (board.isValid(r, c) && (board.getTile(r, c) == 0));
	}
	
	public void updateBlocks()
	{
		if (!isInit)
		{
			newBlock = nextBlock;
			int randNum = (int)(Math.random() * Tetris.NUM_BLOCKS) + 1;
			nextBlock = new Block( randNum ) ;
			
			isInit = true;
			isMoving = true;
			
			//update blocks in next cycle
			return;
		}
		
		for (Vector2D tile : newBlock.getTileArray())
		{	
			int c = (int)tile.getX();
			int r = (int)tile.getY();
			
			isMoving = isOkToMove(r + 1, c);
			if (!isMoving) 
				break;
		}
		
		if (isMoving) 
			newBlock.shiftTiles(1, 0); //move down
		else
		{
			if (placeNewBlock(newBlock) == false)
			{
				isGameOver = true;	
			}
			
			//set init to false, so new block can be made
			isInit = false;
			
			score += 10;
		}
		
	}
	
	public boolean placeNewBlock(Block newBlock)
	{
		for (Vector2D tile : newBlock.getTileArray())
		{
			int newC = (int)tile.getX();
			int newR = (int)tile.getY();
			board.setTile(newR, newC, newBlock.getTypeOfBlock());
			
			// existing blocks piled up 
			// new block can't be placed
			if (newR <= 1) 
			{
				return false;
			}
		}
		
		// new block was placed successfully
		return true;
	}
		
	public void clearLine()
	{
		// don't bother checking if the block is still moving
		if (isMoving)
			return;
		
		// check from bottom to top
		for (int r = board.getNumRows() - 1; r >= 0; r--)
		{
			//check each tile in a row
			if (board.isLineFilled(r))
			{
				board.clearRow(r);

				// the row of blocks above falls
				// check this row again to determine whether it needs to be cleared again
				board.shiftRowDown(r);
				r++;
				
				updateGameScore();
			}
		}
	}
	
	public void updateGameScore()
	{
		linesCleared++;
		
		score += 40*(numLevels+1);
		
		if (linesCleared % 10 == 0)
		{
			// increase game speed
			if (timeUntilNextUpdate > 100) 
				timeUntilNextUpdate -= 25;
			else
				if (timeUntilNextUpdate > 50)
					timeUntilNextUpdate -= 10;
			
			numLevels++;
		}
	}
}
