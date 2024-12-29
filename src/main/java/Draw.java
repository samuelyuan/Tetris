import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Draw extends Application implements Runnable {
    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 500;
    public static final int TILE_WIDTH = 25;
    public static final int TILE_HEIGHT = 25;
    private int LEFT_BOUND_X = 150;
    private int RIGHT_BOUND_X = 400;

    // img data
    private Image[] img = new Image[10];
    private final int IMGID_BG = 0;
    private final int IMGID_RED = 1;
    private final int IMGID_GREEN = 2;
    private final int IMGID_ORANGE = 3;
    private final int IMGID_PURPLE = 4;
    private final int IMGID_YELLOW = 5;
    private final int IMGID_CYAN = 6;
    private final int IMGID_BLUE = 7;

    private Tetris tetrisGame = new Tetris(20, 10);
    private boolean gameBegun = false;
    private int timeUntilStart = 3000;
    private boolean gamePaused = false;
    private Thread thread;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        loadImages();

        Scene scene = new Scene(new StackPane(canvas));
        scene.setOnKeyPressed(this::handleKeyPress);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Tetris");
        primaryStage.show();

        thread = new Thread(() -> {
            while (timeUntilStart >= 0) {
                draw(gc);
                try {
                    Thread.sleep(1000);
                    timeUntilStart -= 1000;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gameBegun = true;

            while (true) {
                if (!gamePaused) {
                    tetrisGame.mainLoop();
                }
                draw(gc);
                try {
                    Thread.sleep(tetrisGame.getTimeUntilNextUpdate());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void loadImages() {
        try {
            img[IMGID_BG] = new Image(getClass().getResourceAsStream("/img/bg.png"));
            img[IMGID_RED] = new Image(getClass().getResourceAsStream("/img/tile_red.png"));
            img[IMGID_GREEN] = new Image(getClass().getResourceAsStream("/img/tile_green.png"));
            img[IMGID_ORANGE] = new Image(getClass().getResourceAsStream("/img/tile_orange.png"));
            img[IMGID_PURPLE] = new Image(getClass().getResourceAsStream("/img/tile_purple.png"));
            img[IMGID_YELLOW] = new Image(getClass().getResourceAsStream("/img/tile_yellow.png"));
            img[IMGID_CYAN] = new Image(getClass().getResourceAsStream("/img/tile_cyan.png"));
            img[IMGID_BLUE] = new Image(getClass().getResourceAsStream("/img/tile_blue.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void draw(GraphicsContext gc) {
        clearScreen(gc, javafx.scene.paint.Color.GRAY);

        drawBoard(gc);
        drawFallingBlock(gc);

        // draw extra information on the right panel
        drawNextBlock(gc);
        drawScore(gc);

        if (!gameBegun) {
            drawTimerText(gc);
        }

        if (tetrisGame.getIsGameOver()) {
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.fillText("GAME OVER!", SCREEN_WIDTH / 2 - 25, SCREEN_HEIGHT / 2);
        }

        if (gamePaused) {
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.fillText("PAUSED!", SCREEN_WIDTH / 2 - 25, SCREEN_HEIGHT / 2);
        }
    }

    private void clearScreen(GraphicsContext gc, javafx.scene.paint.Color colorToClear) {
        gc.setFill(colorToClear);
        gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void drawBoard(GraphicsContext gc) {
        Map board = tetrisGame.getBoard();
        for (int r = 0; r < board.getNumRows(); r++) {
            for (int c = 0; c < board.getNumCols(); c++) {
                Image tileImg = getBlockImage(board.getTile(r, c));
                int x = c * TILE_WIDTH + LEFT_BOUND_X;
                int y = r * TILE_HEIGHT;
                gc.drawImage(tileImg, x, y, TILE_WIDTH, TILE_HEIGHT);
                if (board.getTile(r, c) != 0) {
                    gc.setStroke(javafx.scene.paint.Color.BLACK);
                    gc.strokeRect(x, y, TILE_WIDTH, TILE_HEIGHT);
                }
            }
        }
    }

    private void drawFallingBlock(GraphicsContext gc) {
        Block currentBlock = tetrisGame.getCurrentBlock();
        if (currentBlock == null)
            return;

        for (Vector2D tile : currentBlock.getTileArray()) {
            Image tileImg = getBlockImage(currentBlock.getTypeOfBlock());
            int x = (int) tile.getX() * TILE_WIDTH + LEFT_BOUND_X;
            int y = (int) tile.getY() * TILE_HEIGHT;
            gc.drawImage(tileImg, x, y, TILE_WIDTH, TILE_HEIGHT);
        }
    }

    private void drawNextBlock(GraphicsContext gc) {
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillText("Next Block: ", RIGHT_BOUND_X + 10, 120);
        for (Vector2D tile : tetrisGame.getNextBlock().getTileArray()) {
            int c = (int) tile.getX();
            int r = (int) tile.getY();
            Image tileImg = getBlockImage(tetrisGame.getNextBlock().getTypeOfBlock());
            int x = (c + 13) * TILE_WIDTH;
            int y = r * TILE_HEIGHT + 140;
            gc.drawImage(tileImg, x, y, TILE_WIDTH, TILE_HEIGHT);
        }
    }

    private void drawScore(GraphicsContext gc) {
        gc.setFill(javafx.scene.paint.Color.WHITE);
        int scoreX = RIGHT_BOUND_X + 10;
        int scoreY = SCREEN_HEIGHT / 2 + 120;
        gc.fillText("Score: " + tetrisGame.getScore(), scoreX, scoreY);
        gc.fillText("Level: " + tetrisGame.getNumLevels(), scoreX, scoreY + 20);
        gc.fillText("Lines cleared: " + tetrisGame.getLinesCleared(), scoreX, scoreY + 40);
    }

    private void drawTimerText(GraphicsContext gc) {
        gc.setFill(javafx.scene.paint.Color.WHITE);
        if (timeUntilStart > 0) {
            gc.fillText("" + timeUntilStart / 1000, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        } else {
            gc.fillText("START!", SCREEN_WIDTH / 2 - 50, SCREEN_HEIGHT / 2);
        }
    }

    private Image getBlockImage(int blockType) {
        int imgId = 0;
        switch (blockType) {
            case Block.BlockTypeI:
                imgId = IMGID_ORANGE;
                break;
            case Block.BlockTypeO:
                imgId = IMGID_RED;
                break;
            case Block.BlockTypeT:
                imgId = IMGID_YELLOW;
                break;
            case Block.BlockTypeJ:
                imgId = IMGID_PURPLE;
                break;
            case Block.BlockTypeL:
                imgId = IMGID_BLUE;
                break;
            case Block.BlockTypeS:
                imgId = IMGID_CYAN;
                break;
            case Block.BlockTypeZ:
                imgId = IMGID_GREEN;
                break;
            default:
                imgId = IMGID_BG;
                break;
        }
        return img[imgId];
    }

    private void handleKeyPress(KeyEvent event) {
        if (gamePaused == false) {
            if (!tetrisGame.getIsMoving())
                return;

            if (event.getCode() == KeyCode.RIGHT) {
                tetrisGame.moveBlock(0, 1);
            } else if (event.getCode() == KeyCode.LEFT) {
                tetrisGame.moveBlock(0, -1);
            } else if (event.getCode() == KeyCode.DOWN) {
                tetrisGame.moveBlock(1, 0);
            } else if (event.getCode() == KeyCode.UP) {
                tetrisGame.rotateBlock();
            }
        }

        if (event.getCode() == KeyCode.P && gameBegun) {
            gamePaused = !gamePaused;
        }
    }

    @Override
    public void run() {
        // This method is not used in JavaFX as the game loop is handled in the start
        // method.
    }
}