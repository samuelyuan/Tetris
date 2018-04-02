public class Map 
{
	private int [][] map;
	private int numRows, numCols;
	
	public Map( int numRows, int numCols )
	{
		this.numRows = numRows;
		this.numCols = numCols;
		map = new int [numRows][numCols];
		
		clear();
	}

	public int getTile( int r, int c ) 				
	{ 
		if (isValid( r, c ))
			return map[r][c]; 
		else
			return -10000;
	}
	
	public void setTile( int r, int c, int tile ) 	
	{ 
		if (isValid(r, c))
			map[r][c] = tile; 
	}
	
	public int getNumRows( ) 						{ return numRows; }
	public int getNumCols( ) 						{ return numCols; }
	
	public boolean isValid( int r, int c ) 
	{
		if (r < 0 || c < 0 || r >= numRows || c >= numCols)
			return false;
		else
			return true;
	}
	
	public void clear( ) 
	{
		for (int r = 0; r < numRows; r++)
			clearRow( r );
	}
	
	public void clearRow( int rowNum )
	{
		for (int c = 0; c < numCols; c++)
			map[rowNum][c] = 0;
	}
}
