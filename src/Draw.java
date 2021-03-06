import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Draw extends Applet implements KeyListener, Runnable
{
	//drawing data
	public static final int SCREEN_WIDTH = 600;
	public static final int SCREEN_HEIGHT = 500;
	public static final int TILE_WIDTH = 25;
	public static final int TILE_HEIGHT = 25;
	private int LEFT_BOUND_X = 150;
	private int RIGHT_BOUND_X = 400;

	private Image offscreenImage;
	private Graphics2D offscr;
	private int width, height;
	
	//img data
	private Image [] img = new Image[10];
	private final int IMGID_BG = 0;
	private final int IMGID_RED = 1;
	private final int IMGID_GREEN = 2;
	private final int IMGID_ORANGE = 3;
	private final int IMGID_PURPLE = 4;
	private final int IMGID_YELLOW = 5;
	private final int IMGID_CYAN = 6;
	private final int IMGID_BLUE = 7;
	
	//input data
	public static final int KEY_MOVE_LEFT = KeyEvent.VK_LEFT;
	public static final int KEY_MOVE_RIGHT = KeyEvent.VK_RIGHT;
	public static final int KEY_MOVE_DOWN = KeyEvent.VK_DOWN;
	public static final int KEY_ROTATE = KeyEvent.VK_UP;

	//runtime settings
	private Thread thread;
	
	//game data
	private Tetris tetrisGame = new Tetris(20, 10);

	private boolean gameBegun = false;
	private int timeUntilStart = 3000;
	private boolean gamePaused = false;
	
	public void init()
	{		
		thread = new Thread(this);
		thread.start();
		
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

		addKeyListener(this);
		setFocusable(true);
		
		//screen
		width = getWidth();
		height = getHeight();
		offscreenImage = createImage(width, height);
		offscr = (Graphics2D) offscreenImage.getGraphics();
		
		loadImages();
	}
	
	public void loadImages()
	{
		try
		{
			img[IMGID_BG] = getImage(getDocumentBase(), "img/bg.png");
			img[IMGID_RED] = getImage(getDocumentBase(), "img/tile_red.png");
			img[IMGID_GREEN] = getImage(getDocumentBase(), "img/tile_green.png");
			img[IMGID_ORANGE] = getImage(getDocumentBase(), "img/tile_orange.png");
			img[IMGID_PURPLE] = getImage(getDocumentBase(), "img/tile_purple.png");
			img[IMGID_YELLOW] = getImage(getDocumentBase(), "img/tile_yellow.png");
			img[IMGID_CYAN] = getImage(getDocumentBase(), "img/tile_cyan.png");
			img[IMGID_BLUE] = getImage(getDocumentBase(), "img/tile_blue.png");
		} 
		catch (Exception e) 
		{ 
			System.out.println(e.getMessage());
		}
	}
	
	public void paint(Graphics g)
	{
		clearScreen(Color.GRAY);
		
		Font font;
	
		font = new Font("serif", Font.PLAIN, 20);
		offscr.setFont(font);
		
		drawBoard();
		drawFallingBlock();	
		
		// draw extra information on the right panel
		drawNextBlock();
		drawScore();
		
		if (gameBegun == false)
		{
			drawTimerText();
		}
		
		if (tetrisGame.getIsGameOver() == true)
		{
			font = new Font("serif", Font.PLAIN, 40);
			offscr.setFont(font);
		
			offscr.setColor(Color.WHITE);
			offscr.drawString("GAME OVER!", SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2);
		}
		
		if (gamePaused == true)
		{
			font = new Font("serif", Font.PLAIN, 40);
			offscr.setFont(font);
		
			offscr.setColor(Color.WHITE);
			offscr.drawString("PAUSED!", SCREEN_WIDTH/2 - 50, SCREEN_HEIGHT/2);
		}
		
		//send to front buffer
		g.drawImage(offscreenImage, 0, 0, this);
	}
	
	public void clearScreen( Color colorToClear )
	{
		//clear back buffer to black
		offscr.setColor(colorToClear);
		offscr.fillRect(0, 0, width, height);
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void run() 
	{
		while (timeUntilStart >= 0)
		{
			repaint();
			
			try
			{
				Thread.sleep(1000);
				timeUntilStart -= 1000;
			} catch (InterruptedException e) { }
		}
		
		gameBegun = true;
				
		while (true)
		{	
			if (gamePaused)
			{
				try
				{
					Thread.sleep(tetrisGame.getTimeUntilNextUpdate());
				} catch (InterruptedException e) { }
				
				continue;
			}
			
			//update screen
			repaint();
			
			tetrisGame.mainLoop();
			
			//screen refresh
			try
			{
				Thread.sleep(tetrisGame.getTimeUntilNextUpdate());
			} catch (InterruptedException e) { }
		}
	}
	
	public void handleKeyRotate(KeyEvent e, int keyCode)
	{
		if (e.getKeyCode() != keyCode)
			return;

		tetrisGame.rotateBlock();
	}
	
	public void handleKeyMove(KeyEvent e, int keyCode, int shiftR, int shiftC)
	{
		if (e.getKeyCode() != keyCode)
			return;
		
		tetrisGame.moveBlock(shiftR, shiftC);
	}

	public void keyPressed(KeyEvent e) 
	{
		if (gamePaused == false)
		{
			if (!tetrisGame.getIsMoving())
				return;

			handleKeyMove(e, KEY_MOVE_RIGHT, 0, 1);
			handleKeyMove(e, KEY_MOVE_LEFT, 0, -1);
			handleKeyMove(e, KEY_MOVE_DOWN, 1, 0);
			handleKeyRotate(e, KEY_ROTATE);
		}
		if (e.getKeyCode() == KeyEvent.VK_P && gameBegun == true)
			gamePaused = !gamePaused;
		
		repaint();
	}

	public void keyReleased(KeyEvent arg0) { }
	public void keyTyped(KeyEvent arg0) { }
		
	public Image getBlockImage(int blockType)
	{
		int imgId = 0;
		switch (blockType)
		{
			case Block.BlockTypeI: imgId = IMGID_ORANGE; break;
			case Block.BlockTypeO: imgId = IMGID_RED; break;
			case Block.BlockTypeT: imgId = IMGID_YELLOW; break;
			case Block.BlockTypeJ: imgId = IMGID_PURPLE; break;
			case Block.BlockTypeL: imgId = IMGID_BLUE; break;
			case Block.BlockTypeS: imgId = IMGID_CYAN; break;
			case Block.BlockTypeZ: imgId = IMGID_GREEN; break;
			default: imgId = IMGID_BG; break;
		}
		return img[imgId];
	}
	
	public void drawBoard()
	{
		Map board = tetrisGame.getBoard();
		
		//board tiles
		for (int r = 0; r < board.getNumRows(); r++)
		{
			for (int c = 0; c < board.getNumCols(); c++)
			{
				//draw tiles
				Image tileImg = getBlockImage(board.getTile(r, c));
				int x = c * TILE_WIDTH + LEFT_BOUND_X;
				int y = r * TILE_HEIGHT;
				offscr.drawImage(tileImg, x, y, TILE_WIDTH, TILE_HEIGHT, Color.WHITE, this);
				
				//draw outline of tiles
				if (board.getTile(r, c) != 0)
				{
					offscr.setColor(Color.BLACK);
					offscr.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
				}
			}
		}
		
		//board border
		offscr.setColor(Color.WHITE);
		offscr.drawLine(LEFT_BOUND_X, 0, LEFT_BOUND_X, SCREEN_HEIGHT);
		offscr.drawLine(RIGHT_BOUND_X, 0, RIGHT_BOUND_X, SCREEN_HEIGHT);
	}
	
	public void drawFallingBlock()
	{
		Block currentBlock = tetrisGame.getCurrentBlock();
		if (currentBlock == null)
			return;
		
		for (Vector2D tile : currentBlock.getTileArray())
		{
			Image tileImg = getBlockImage(currentBlock.getTypeOfBlock());
			int x = (int)tile.getX() * TILE_WIDTH + LEFT_BOUND_X;
			int y = (int)tile.getY() * TILE_HEIGHT;
			offscr.drawImage(tileImg, x, y, TILE_WIDTH, TILE_HEIGHT, Color.WHITE, this);
		}
	}
	
	public void drawNextBlock()
	{
		// display Next Block data
		offscr.setColor(Color.WHITE);
		offscr.drawString("Next Block: ", RIGHT_BOUND_X + 10, 120);
		for (Vector2D tile : tetrisGame.getNextBlock().getTileArray())
		{
			int c = (int) tile.getX();
			int r = (int) tile.getY();
			
			Image tileImg = getBlockImage(tetrisGame.getNextBlock().getTypeOfBlock());
			int x = (c+13) * TILE_WIDTH;
			int y = r * TILE_HEIGHT + 140;
			offscr.drawImage(tileImg, x, y, TILE_WIDTH, TILE_HEIGHT , Color.WHITE, this);
		}
	}
	
	public void drawScore()
	{
		// display score data
		int scoreX = RIGHT_BOUND_X + 10;
		int scoreY = SCREEN_HEIGHT/2 + 120;
		offscr.setColor(Color.WHITE);
		offscr.drawString("Score: " 		+ tetrisGame.getScore(), 		scoreX, scoreY);
		offscr.drawString("Level: " 		+ tetrisGame.getNumLevels(), 	scoreX, scoreY + 20);
		offscr.drawString("Lines cleared: " + tetrisGame.getLinesCleared(), scoreX, scoreY + 40);
	}
	
	public void drawTimerText()
	{
		Font font = new Font("serif", Font.PLAIN, 40);
		offscr.setFont(font);
		
		offscr.setColor(Color.WHITE);
		if (timeUntilStart > 0)
			offscr.drawString("" + timeUntilStart/1000, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
		else
			offscr.drawString("START!", SCREEN_WIDTH/2 - 50, SCREEN_HEIGHT/2);
	}
}
