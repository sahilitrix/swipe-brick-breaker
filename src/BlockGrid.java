
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class BlockGrid extends JComponent {
	
	private GameSprite[][] grid;
	private JLabel score;
	private static final int PADDING = 2;
	private static final int BLOCK_SIDE_LENGTH = 40;
	private static final int COIN_DIAMETER = 12;
	private static final int GRID_WIDTH = 8;
	private static final int GRID_HEIGHT = 11;
	
	public BlockGrid(JLabel score) {
		this.score = score;
		grid = new GameSprite[GRID_HEIGHT][GRID_WIDTH];
		initializeGrid();
	}
	
	public BlockGrid(int height, int width, JLabel score) {
		this.score = score;
		grid = new GameSprite[height][width];
		initializeGrid();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				grid[r][c].draw(g);
			}
		}
	}
	
	@Override
	public Dimension getSize() {
		return new Dimension(grid[0].length*(BLOCK_SIDE_LENGTH+PADDING)+PADDING, 
							grid.length*(BLOCK_SIDE_LENGTH+PADDING)+PADDING);
	}
	
	@Override
	public int getWidth() {
		return grid[0].length*(BLOCK_SIDE_LENGTH+PADDING)+PADDING;
	}
	
	@Override
	public int getHeight() {
		return grid.length*(BLOCK_SIDE_LENGTH+PADDING)+PADDING;
	}
	
	public GameSprite[][] getGrid() {
		return grid;
	} 
	
	public GameSprite[][] getGridCopy(){
		GameSprite[][] copy = new GameSprite[grid.length][grid[0].length];
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				copy[r][c] = grid[r][c];
			}
		}
		return copy;
	}
	
	private GameSprite selectRandomBlock(int x, int y) {
		//return either an EmptyBlock, NumBlockSprite, or CoinSprite randomly
		//CoinSprite is generated with a lower probability than the other two
		int emptyProbability = 4;
		int numBlockProbability = 4;
		int coinProbability = 1;
		int totalBasket = emptyProbability + numBlockProbability + coinProbability;
		int randomNumber = (int) (Math.random() * totalBasket);
		
		GameSprite [] choices = new GameSprite[totalBasket];
		for (int i = 0; i < emptyProbability; i++) {
			choices[i] = new EmptySprite(x, y, BLOCK_SIDE_LENGTH, BLOCK_SIDE_LENGTH);
		}
		for (int i = 0; i < numBlockProbability; i++) {
			choices[i+emptyProbability] = new NumBlockSprite(x, y, BLOCK_SIDE_LENGTH, score);
		}
		for (int i = 0; i < coinProbability; i++) {
			choices[i+emptyProbability+numBlockProbability] = new CoinSprite(x, y, 
															  COIN_DIAMETER, BLOCK_SIDE_LENGTH);
		}

		return choices[randomNumber];
	}
	
	private void initializeGrid() {
		int xPos = PADDING;
		int yPos = PADDING;
		int numInitialEmptyRows = (int) Math.ceil((grid.length / 2.0));
		int endRowForRandomSprites = grid.length - numInitialEmptyRows;
		//initialize top rows with random blocks
		for (int r = 0; r < endRowForRandomSprites; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				grid[r][c] = selectRandomBlock(xPos, yPos);
				xPos += BLOCK_SIDE_LENGTH + PADDING;
			}
			xPos = PADDING;
			yPos += BLOCK_SIDE_LENGTH + PADDING;
		}
		//leave the bottom few rows empty on game start
		for (int r = 0; r < numInitialEmptyRows; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				grid[r+endRowForRandomSprites][c] = new EmptySprite(xPos, yPos, 
										 BLOCK_SIDE_LENGTH, BLOCK_SIDE_LENGTH);
				xPos += BLOCK_SIDE_LENGTH + PADDING;
			}
			xPos = PADDING;
			yPos += BLOCK_SIDE_LENGTH + PADDING;
		}
	}
	
	public void nextRound() {			
		//shift all the elements in the grid down by 1 row
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				int currentY = grid[r][c].getY();
				grid[r][c].setY(currentY+BLOCK_SIDE_LENGTH+PADDING);
			}
		}
		//store all the shifted elements in a temp 2d array
		GameSprite[][] copy = new GameSprite[grid.length][grid[0].length];
		for (int r = 0; r < copy.length; r++) {
			for (int c = 0; c < copy[r].length; c++) {
				copy[r][c] = grid[r][c];
			}
		}
		//shift the actual 2d array down by 1 row, leaving out the bottom row
		for (int r = 1; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				grid[r][c] = copy[r-1][c];
			}
		}
		
		//fill in the top row with new randomly generated blocks
		int xPos = PADDING;
		int yPos = PADDING;
		for (int i = 0; i < grid[0].length; i++) {
			grid[0][i] = selectRandomBlock(xPos, yPos);
			xPos += BLOCK_SIDE_LENGTH + PADDING;
		}	
		incrementScore();
	}
	
	
	public boolean isGameOver() {
		//check if bottom row is empty
		int lastRow = grid.length - 1;
		for (int i = 0; i < grid[lastRow].length; i++) {
			if (grid[lastRow][i] instanceof NumBlockSprite) {
				return true;
			}
		}
		return false;
	}
	
	private void incrementScore() {
		int currentScore = Integer.parseInt(score.getText());
		score.setText(currentScore+1+"");
	}
}
