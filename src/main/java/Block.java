public class Block
{
	public static final int BlockTypeI = 1;
	public static final int BlockTypeO = 2;
	public static final int BlockTypeT = 3;
	public static final int BlockTypeJ = 4;
	public static final int BlockTypeL = 5;
	public static final int BlockTypeS = 6;
	public static final int BlockTypeZ = 7;
	
	private int typeOfBlock;
	private Vector2D [] tiles = new Vector2D [4]; //tile coordinates represented as (r, c) where r = y, c = x.

	public int getTypeOfBlock() { return typeOfBlock; }
	public Vector2D getTile(int index) { return tiles[index]; }
	public Vector2D [] getTileArray() { return tiles; }
	
	public void shiftTiles(int shiftR, int shiftC) {
		for (Vector2D tile: tiles) {
			tile.setX(tile.getX() + shiftC);
			tile.setY(tile.getY() + shiftR);
		}
	}
	
	public void rotateTiles(Vector2D center) {
		for (Vector2D tile: tiles) {
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
	
	public Block(int typeOfBlock) {
		this.typeOfBlock = typeOfBlock;
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new Vector2D(0, 0);
		}

		switch (typeOfBlock) {
			case BlockTypeI:
				tiles[0] = new Vector2D(5, 0);
				tiles[1] = new Vector2D(5, 1);
				tiles[2] = new Vector2D(5, 2);
				tiles[3] = new Vector2D(5, 3);
				break;
				
			case BlockTypeO:
				tiles[0] = new Vector2D(5, 0);
				tiles[1] = new Vector2D(5, 1);
				tiles[2] = new Vector2D(6, 0);
				tiles[3] = new Vector2D(6, 1);
				break;
				
			case BlockTypeT:
				tiles[0] = new Vector2D(5, 0);
				tiles[1] = new Vector2D(5, 1);
				tiles[2] = new Vector2D(4, 1);
				tiles[3] = new Vector2D(6, 1);
				break;
				
			case BlockTypeJ:
				tiles[0] = new Vector2D(4, 0);
				tiles[1] = new Vector2D(4, 1);
				tiles[2] = new Vector2D(5, 1);
				tiles[3] = new Vector2D(6, 1);
				break;
				
			case BlockTypeL:
				tiles[0] = new Vector2D(6, 0);
				tiles[1] = new Vector2D(6, 1);
				tiles[2] = new Vector2D(5, 1);
				tiles[3] = new Vector2D(4, 1);
				break;
				
			case BlockTypeS:
				tiles[0] = new Vector2D(6, 0);
				tiles[1] = new Vector2D(5, 0);
				tiles[2] = new Vector2D(5, 1);
				tiles[3] = new Vector2D(4, 1);
				break;
				
			case BlockTypeZ:
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
