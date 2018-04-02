import java.awt.Color;

public class Block
{
	private int typeOfBlock;
	private Color color;
	private Vector2D [] tiles = new Vector2D [4]; //tile coordinates represented as (r, c) where r = y, c = x.

	public int getTypeOfBlock() { return typeOfBlock; }
	public Color getColor() { return color; }
	public Vector2D getTile(int index) { return tiles[index]; }
	public Vector2D [] getTileArray() { return tiles; }
	
	public void shiftTiles( int shiftR, int shiftC )
	{
		for ( Vector2D tile: tiles )
		{
			tile.setX(tile.getX() + shiftC);
			tile.setY(tile.getY() + shiftR);
		}
	}
	
	public void rotateTiles( Vector2D center )
	{
		for ( Vector2D tile: tiles )
		{
			//shift by center
			int oldX = (int) (tile.getX() - center.getX());
			int oldY = (int) (tile.getY() - center.getY());
			
			//rotate 90 degrees around center
			//and undo shift by center	
			tile.setX(-oldY + center.getX());
			tile.setY(oldX + center.getY());
		}
	}
	
	//Tetris block configurations
	//1 - | | | |
	//2 - | |
	//    | |
	//3 -   |
	//    | | |
	//4 - |  
	//    | | |
	//5 -     |
	//    | | |
	//6 -   | |
	//    | |
	//7 - | |
	//      | |
	
	public Block( int typeOfBlock )
	{
		this.typeOfBlock = typeOfBlock;
		for (int i = 0; i < tiles.length; i++)
			tiles[i] = new Vector2D(0, 0);

		switch ( typeOfBlock )
		{
			case 1:
				color = Color.ORANGE;
				tiles[0] = new Vector2D(5, 0);
				tiles[1] = new Vector2D(5, 1);
				tiles[2] = new Vector2D(5, 2);
				tiles[3] = new Vector2D(5, 3);
				break;
				
			case 2:
				color = Color.RED;
				tiles[0] = new Vector2D(5, 0);
				tiles[1] = new Vector2D(5, 1);
				tiles[2] = new Vector2D(6, 0);
				tiles[3] = new Vector2D(6, 1);
				break;
				
			case 3:
				color = Color.YELLOW;
				tiles[0] = new Vector2D(5, 0);
				tiles[1] = new Vector2D(5, 1);
				tiles[2] = new Vector2D(4, 1);
				tiles[3] = new Vector2D(6, 1);
				break;
				
			case 4:
				color = new Color(255, 0, 255);
				tiles[0] = new Vector2D(4, 0);
				tiles[1] = new Vector2D(4, 1);
				tiles[2] = new Vector2D(5, 1);
				tiles[3] = new Vector2D(6, 1);
				break;
				
			case 5:
				color = Color.BLUE;
				tiles[0] = new Vector2D(6, 0);
				tiles[1] = new Vector2D(6, 1);
				tiles[2] = new Vector2D(5, 1);
				tiles[3] = new Vector2D(4, 1);
				break;
				
			case 6:
				color = Color.CYAN;
				tiles[0] = new Vector2D(6, 0);
				tiles[1] = new Vector2D(5, 0);
				tiles[2] = new Vector2D(5, 1);
				tiles[3] = new Vector2D(4, 1);
				break;
				
			case 7:
				color = Color.GREEN;
				tiles[0] = new Vector2D(4, 0);
				tiles[1] = new Vector2D(5, 0);
				tiles[2] = new Vector2D(5, 1);
				tiles[3] = new Vector2D(6, 1);
				break;
				
			default:
				break;
		}
	}
}
