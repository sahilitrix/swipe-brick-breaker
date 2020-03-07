import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.swing.JLabel;

import org.junit.Test;

/** 
 *  You can use this file (and others) to test your
 *  implementation.
 */

public class GameTest {
    
    @Test
    public void test2DGridCreationWithEvenNumberHeight() {
    	JLabel score = new JLabel("2");
    	BlockGrid grid = new BlockGrid(10, 5, score);
    	GameSprite[][] arr = grid.getGrid();
    	//make sure that all objects in grid are not null
    	for (int r = 0; r < arr.length; r++) {
    		for (int c = 0; c < arr[r].length; c++) {
    			if (arr[r][c] == null) {
    				fail();
    			}
    		}
    	}
    	//check that the bottom half of the rows are all EmptySprites
    	//in this case, the bottom 5 rows should all contain EmptySprites
    	for (int r = 5; r < 10; r++) {
    		for (int c = 0; c < 5; c++) {
    			if (!(arr[r][c] instanceof EmptySprite)) {
    				fail();
;    			}
    		}
    	}
    	
    	//check that all the other rows (top 5) have either an EmptySprite, NumBlockSprite
    	//or CoinSprite
    	for (int r = 0; r < 5; r++) {
    		for (int c = 0; c < 5; c++) {
    			if (!(arr[r][c] instanceof EmptySprite) && !(arr[r][c] instanceof CoinSprite)
    					&& !(arr[r][c] instanceof NumBlockSprite)) {
    				fail();
;    			}
    		}
    	}
    }
    
    @Test
    public void test2DGridCreationWithOddNumberHeight() {
    	JLabel score = new JLabel("2");
    	BlockGrid grid = new BlockGrid(11, 5, score);
    	GameSprite[][] arr = grid.getGrid();
    	//make sure that all objects in grid are not null
    	for (int r = 0; r < arr.length; r++) {
    		for (int c = 0; c < arr[r].length; c++) {
    			if (arr[r][c] == null) {
    				fail();
    			}
    		}
    	}
    	//check that the bottom half of the rows are all EmptySprites
    	//in this case, the bottom 6 rows should all contain EmptySprites
    	for (int r = 5; r < 11; r++) {
    		for (int c = 0; c < 5; c++) {
    			if (!(arr[r][c] instanceof EmptySprite)) {
    				fail();
;    			}
    		}
    	}
    	
    	//check that all the other rows (top 5) have either an EmptySprite, NumBlockSprite
    	//or CoinSprite
    	for (int r = 0; r < 5; r++) {
    		for (int c = 0; c < 5; c++) {
    			if (!(arr[r][c] instanceof EmptySprite) && !(arr[r][c] instanceof CoinSprite)
    					&& !(arr[r][c] instanceof NumBlockSprite)) {
    				fail();
;    			}
    		}
    	}
    }
    //check that the grid advanced properly
    @Test
    public void test2DGridAdvance() {
    	JLabel score = new JLabel("2");
    	BlockGrid grid = new BlockGrid(score);
    	
    	while (!grid.isGameOver()) {
        	GameSprite[][] original = grid.getGridCopy();
    		GameSprite[][] advanced = grid.getGrid();

    		grid.nextRound();    	    	
    		//make sure that the top row is newly generated
    		boolean rowIsSame = true;
    		for (int i = 0; i < original[0].length; i++) {
    			if (original[0][i] != advanced[0][i]) {
    				rowIsSame = false;
    			}
    			if (original[0][i] != advanced[1][i]) {
    				rowIsSame = false;
    			}
    		}
    		assertFalse(rowIsSame);
   
    		//make sure that bottom row gets deleted 
    		//and is not contained anywhere else in the grid
    		GameSprite[] bottomRow = original[original.length-1];
    		for (int r = 0; r < advanced.length; r++) {
    			if (bottomRow == advanced[r]) {
    				fail();
    			}
    		}
    		
    		//check that all other rows have shifted down by 1
    		for (int r = 1; r < advanced.length-1; r++) {
    			for (int c = 0; c < advanced[r].length; c++) {
        			if (advanced[r][c] != original[r-1][c]) {
        				fail();
        			}
    			}
    		}
    	}
    	
    	//check that game-over condition has been reached
    	assertTrue(grid.isGameOver());
    }
    
    @Test
    public void test2DGridAdvanceBottomRowEmpty() {
    	JLabel score = new JLabel("2");
    	BlockGrid grid = new BlockGrid(score);
    	
    	GameSprite[][] original = grid.getGridCopy();
    	GameSprite[][] advanced = grid.getGrid();
    	//make the bottom row all empty
    	for (int c = 0; c < advanced[0].length; c++) {
    		advanced[advanced.length-1][c] = new EmptySprite(0, 0, 10, 10);
    	}
    	grid.nextRound();

    	//make sure that the top row is newly generated
    	boolean rowIsSame = true;
    	for (int i = 0; i < original[0].length; i++) {
    		if (original[0][i] != advanced[0][i]) {
    			rowIsSame = false;
    		}
    		if (original[0][i] != advanced[1][i]) {
    			rowIsSame = false;
    		}
    	}
    	assertFalse(rowIsSame);

    	//make sure that bottom row gets deleted 
    	//and is not contained anywhere else in the grid
    	GameSprite[] bottomRow = original[original.length-1];
    	for (int r = 0; r < advanced.length; r++) {
    		if (bottomRow == advanced[r]) {
    			fail();
    		}
    	}

    	//check that all other rows have shifted down by 1
    	for (int r = 1; r < advanced.length-1; r++) {
    		for (int c = 0; c < advanced[r].length; c++) {
    			if (advanced[r][c] != original[r-1][c]) {
    				fail();
    			}
    		}
    	}
    }
    
    @Test
    public void test2DGridAdvanceBottomRowSingleBlock() {
    	JLabel score = new JLabel("2");
    	BlockGrid grid = new BlockGrid(score);
    	
    	GameSprite[][] original = grid.getGridCopy();    	
    	GameSprite[][] advanced = grid.getGrid();

    	//make the bottom row all empty, except for 1
    	for (int c = 0; c < advanced[0].length; c++) {
    		if (c == 1) {
        		advanced[advanced.length-1][c] = new NumBlockSprite(0, 0, 10, score);
    		} else {
    			advanced[advanced.length-1][c] = new EmptySprite(0, 0, 10, 10);
    		}
    	}

    	
    	grid.nextRound();

    	//make sure that the top row is newly generated
    	boolean rowIsSame = true;
    	for (int i = 0; i < original[0].length; i++) {
    		if (original[0][i] != advanced[0][i]) {
    			rowIsSame = false;
    		}
    		if (original[0][i] != advanced[1][i]) {
    			rowIsSame = false;
    		}
    	}
    	assertFalse(rowIsSame);

    	//make sure that bottom row gets deleted 
    	//and is not contained anywhere else in the grid
    	GameSprite[] bottomRow = original[original.length-1];
    	for (int r = 0; r < advanced.length; r++) {
    		if (bottomRow == advanced[r]) {
    			fail();
    		}
    	}

    	//check that all other rows have shifted down by 1
    	for (int r = 1; r < advanced.length-1; r++) {
    		for (int c = 0; c < advanced[r].length; c++) {
    			if (advanced[r][c] != original[r-1][c]) {
    				fail();
    			}
    		}
    	}
    }
    
    @Test
    public void test2DGridAdvanceTopRowEmpty() {
    	JLabel score = new JLabel("2");
    	BlockGrid grid = new BlockGrid(score);
    	
    	GameSprite[][] advanced = grid.getGrid();

    	//make the top row all empty
    	for (int c = 0; c < advanced[0].length; c++) {
        	advanced[0][c] = new EmptySprite(0, 0, 10, 10);
    	}
    	
    	GameSprite[][] original = grid.getGridCopy();    	
    	
    	grid.nextRound();

    	//make sure that the top row is newly generated
    	boolean rowIsSame = true;
    	for (int i = 0; i < original[0].length; i++) {
    		if (original[0][i] != advanced[0][i]) {
    			rowIsSame = false;
    		}
    		if (original[0][i] != advanced[1][i]) {
    			rowIsSame = false;
    		}
    	}
    	assertFalse(rowIsSame);

    	//make sure that bottom row gets deleted 
    	//and is not contained anywhere else in the grid
    	GameSprite[] bottomRow = original[original.length-1];
    	for (int r = 0; r < advanced.length; r++) {
    		if (bottomRow == advanced[r]) {
    			fail();
    		}
    	}

    	//check that all other rows have shifted down by 1
    	for (int r = 1; r < advanced.length-1; r++) {
    		for (int c = 0; c < advanced[r].length; c++) {
    			if (advanced[r][c] != original[r-1][c]) {
    				fail();
    			}
    		}
    	}
    }
    
    @Test
    public void test2DGridAdvanceTopRowSingleton() {
    	JLabel score = new JLabel("2");
    	BlockGrid grid = new BlockGrid(score);
    	
    	GameSprite[][] advanced = grid.getGrid();

    	//make the top row all empty, except for one
    	for (int c = 0; c < advanced[0].length; c++) {
    		if (c == 1) {
    			advanced[0][c] = new NumBlockSprite(0, 0, 10, score);
    		} else {
            	advanced[0][c] = new EmptySprite(0, 0, 10, 10);
    		}
    	}
    	
    	GameSprite[][] original = grid.getGridCopy();    	
    	
    	grid.nextRound();

    	//make sure that the top row is newly generated
    	boolean rowIsSame = true;
    	for (int i = 0; i < original[0].length; i++) {
    		if (original[0][i] != advanced[0][i]) {
    			rowIsSame = false;
    		}
    		if (original[0][i] != advanced[1][i]) {
    			rowIsSame = false;
    		}
    	}
    	assertFalse(rowIsSame);

    	//make sure that bottom row gets deleted 
    	//and is not contained anywhere else in the grid
    	GameSprite[] bottomRow = original[original.length-1];
    	for (int r = 0; r < advanced.length; r++) {
    		if (bottomRow == advanced[r]) {
    			fail();
    		}
    	}

    	//check that all other rows have shifted down by 1
    	for (int r = 1; r < advanced.length-1; r++) {
    		for (int c = 0; c < advanced[r].length; c++) {
    			if (advanced[r][c] != original[r-1][c]) {
    				fail();
    			}
    		}
    	}
    }
    
    @Test
    public void testNumBlockCollisionDecreasesHealthOnly(){
    	//check that health goes down by 1
    	NumBlockSprite block = new NumBlockSprite(0, 0, 10, new JLabel("0"));
    	PlayerBallSprite ball = new PlayerBallSprite(0, 0, 10, 100, 100);
    	int currHealth = block.getHealth();
    	
    	assertFalse(block.isReadyToBeDestroyed());
    	block.onCollision(ball);
    	assertTrue(block.getHealth() == currHealth - 1);
    	
    	//check that health doesn't go below 0
    	while (block.getHealth() > 0) {
    		block.onCollision(ball);
    	}
    	block.onCollision(ball);
    	assertTrue(block.getHealth() == 0);
    	
    	//check that block is destroyable once health is 0
    	assertTrue(block.isReadyToBeDestroyed());
    }
    
    @Test
    public void testNumBlockCollisionDecreasesHealthAndDestroys(){
    	NumBlockSprite block = new NumBlockSprite(0, 0, 10, new JLabel("0"));
    	PlayerBallSprite ball = new PlayerBallSprite(0, 0, 10, 100, 100);
    	
    	//reduce the health until it is 1
    	while (block.getHealth() > 1) {
    		block.onCollision(ball);
    	}
    	assertTrue(block.getHealth() == 1);
    	assertFalse(block.isReadyToBeDestroyed());
    	
    	//check that health doesn't go below 0
    	while (block.getHealth() > 0) {
    		block.onCollision(ball);
    	}
    	block.onCollision(ball);
    	assertTrue(block.getHealth() == 0);
    	
    	//check that block is destroyable once health is 0
    	assertTrue(block.isReadyToBeDestroyed());
    }
    
    @Test
    public void testCoinCollisionDestroysCoin() {
    	//check that coin is destroyable after collision
    	CoinSprite coin = new CoinSprite(0, 0, 5, 5);
    	PlayerBallSprite ball = new PlayerBallSprite(0, 0, 10, 100, 100);

    	assertFalse(coin.isReadyToBeDestroyed());
    	coin.onCollision(ball);
    	assertTrue(coin.isReadyToBeDestroyed());
    }
    
    @Test
    public void testCoinCollisionIncreasesPlayerBallCount() {
    	PlayingArea area = new PlayingArea(new JLabel("0"), new HighScoreParser("files/highscores.txt"));
    	assertTrue(area.getBallCount() == 1);
    	area.collideWithCoin();
    	assertTrue(area.getBallCount() == 2);
    }
    
    @Test
    public void testScoreIncrement() {
    	JLabel score = new JLabel("0");
    	BlockGrid grid = new BlockGrid(10, 5, score);
    	assertTrue(score.getText().equals("0"));
    	grid.nextRound();
    	assertTrue(score.getText().equals("1"));
    }
    
}
