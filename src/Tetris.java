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
		board = new Map ( numRows, numCols );
		int randNum = (int)(Math.random() * Tetris.NUM_BLOCKS) + 1;
		nextBlock = new Block( randNum ) ;
	}
	
	public boolean isOkToMove( int r, int c )
	{
		if ( board.isValid( r, c ) ) 
		{
			if ( board.getTile( r, c ) == 0 )
				return true;
		}
		
		return false;
	}
	
//	public boolean isOkToRotate( int r, int c, Vector2D center)
//	{
//		//rotating 90 degrees
//		int oldR = (int) (r - center.getY());
//		int oldC = (int) (c - center.getX());
//		
//		int newR = -oldC;
//		int newC = oldR;
//		
//		newR += center.getY();
//		newC += center.getX();
//	
//		//test to make sure that the new position is not out of bounds or collides with another block
//		return isOkToMove( newR - 1, newC - 1 ) && isOkToMove( newR + 1, newC + 1 );
//	}
	
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
		
		for ( Vector2D tile : newBlock.getTileArray() )
		{	
			int c = (int)tile.getX();
			int r = (int)tile.getY();
			
			isMoving = isOkToMove( r + 1, c );
			if (!isMoving) 
				break;
		}
		
		if (isMoving) 
			newBlock.shiftTiles(1, 0); //move down
		else
		{
			for ( Vector2D tile : newBlock.getTileArray() )
			{
				int newC = (int)tile.getX();
				int newR = (int)tile.getY();
				board.setTile( newR, newC, newBlock.getTypeOfBlock() );
				
				//blocks piled up
				if (newR <= 1) 
					isGameOver = true;	
			}
			
			//set init to false, so new block can be made
			isInit = false;
			
			score += 10;
		}
		
	}
	
	public boolean isLineFilled( int rowNum )
	{
		for (int c = 0; c < board.getNumCols(); c++)
		{
			//line isn't filled, since there's an empty space
			if ( board.getTile(rowNum, c) == 0 )
				return false;
		}
		
		return true;
	}
	
	public void clearLine()
	{
		boolean isLineClearable = true;

		//don't bother checking if the block is still moving
		if (isMoving)
			return;
		
		//check from bottom to top
		for (int r = board.getNumRows() - 1; r >= 0; r--)
		{
			//check each tile in a row
			isLineClearable = isLineFilled( r );
			
			if (isLineClearable)
			{
				board.clearRow( r );
				
				//carry everything else down to the ground
				for (int startingY = r - 1; startingY >= 0; startingY--)
				{
					for (int x = 0; x < board.getNumCols(); x++)
						board.setTile( startingY + 1, x, board.getTile( startingY, x ) );
				}
				
				//the row of blocks above falls, so check that row again to determine whether
				//it needs to be cleared again
				r++;
				
				updateGameScore();
			}
			
			//reset vars
			isLineClearable = true;
		}
	}
	
	public void updateGameScore()
	{
		linesCleared++;
		
		score += 40*(numLevels+1);
		
		if (linesCleared % 10 == 0)
		{
			if (timeUntilNextUpdate > 100) 
				timeUntilNextUpdate -= 25;
			else
				if (timeUntilNextUpdate > 50)
					timeUntilNextUpdate -= 10;
			
			numLevels++;
		}
	}
}
