=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: sahitpen
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2-D Arrays
  
	A Swipe-Brick Breaker grid is a rectangular space that is filled with either empty space, coins for the
	player to collect, or number blocks that the player has to weaken and destroy. I represented this grid
	with a 2D array of type GameSprite, filled with subtypes that are either EmptySprite objects,
	CoinSprite objects, or NumBlockSprite objects. To populate each next level, I shifted the 2D
	array grid down one row, with the bottom row getting deleted and the top row getting populated with random new GameSprite
	objects that are either CoinSprite objects, EmptySprite objects, or NumBlockSprite objects. I also had 
	to iterate through the 2-D array to see if the bottom row contained any NumBlockSprite objects, in which
	case it is game-over. A 2-D array is appropriate because it allows for easily modeling of the grid, where
	there are rows and columns.

  2. Collections
  
	In Swipe Brick Breaker, a player is represented by a series of balls. You start the game with one ball
	and collect coins to increase the number of balls you have. I used a LinkedList that stores objects
	of type PlayerBallSprite to keep a list of all the player balls. If the player collides with a coin
	(CoinSprite object), a new ball (PlayerBallSprite) is added to the LinkedList. LinkedList is the
	best choice because I only need to add to the tail every time and the time complexity is only O(1),
	which is very efficient. Also, there are duplicates (every ball is the same), so using a Set wouldn't
	make sense. An array wouldn’t make sense either because the size isn’t initially known. Balls never
	get removed from the list. The LinkedList will simply get reinitialized on restart of the game.

  3. File I/O
  
    I used File I/O to implement high-scores in my game. The first three times the game is played, the
	the three scores are written to the 'highscores.txt' file. The format of the file is the following,
	"firstName-lastName-pennkey-score", where each attribute is seperated by a dash. On each gameplay,
	I read from the file to get the current top-three high scores (or less if there are less than three). 
	If the user’s score after a game-play is higher than any of these top three high scores, I request for 
	the user's information using JOptionPane input dialogs and write this information to the 'highscores.txt'
	file. The lowest high-scores is replaced and the three new scores are written to the file. 
	The three highscores that I read from the file are displayed to the user, sorted in order from highest
	to lowest score. The scores will also have first-name, last-name, and pennkey attributes that are 
	written to and read from the file.

  4. Testing
  
  	I used JUnit Testing to check whether the states of the GameSprite 2-D array, the PlayerBallSprite LinkedList,
  	the game score, and the NumBlockSprite health update properly. I checked if the 2D array gets initialized 
  	properly, and after each round (a call to the nextRound() method), if each row shifts down properly to the 
  	next round's state. I checked edge cases where the top and bottom rows had empty or single blocks. I tested 
  	if the NumBlockSprite's health decreases on collision and if the block properly destroys when health is 0.
  	I checked if the score increments on each round. I also tested if the LinkedList gets updated properly when 
  	a new ball gets added. I am also testing the FileIO. I test for edge cases where the improper file is given 
  	to the HighScoreReader. I test to see if the proper writing and reading is done with the given txt file. I 
  	test for edge cases where the file is initially empty or full.

  5. Inheritance/Subtyping (extra concept that I ended up using)
  
	Abstract class GameSprite has implemented setter and getter methods for width, height, xPosition, and yPosition. 
	It has an unimplemented draw() method that its subclasses implement. EmptySprite is a subclass that 
	extends GameSprite. It implements the draw() method to draw a transparent rectangle. PlayerBallSprites are also GameSprites 
	but they have an additional move() method and additional getter methods for the velocities. PlayerBallSprite also has a bounce() 
	method, an isActive state to check if the ball is moving, and launchReady state to check if the ball is ready for launch.
	CollisionSprite is a GameSprite with an additional onCollison() method to handle collision behavior. 
	NumBlockSprite and CoinSprite extend CollisionSprite and implement the draw() and onCollision() methods in different ways
	(so dynamic dispatch will occur). NumBlockSprite reduces its health on collision while CoinSprite self destructs on collision.
	Subtyping and inheritance is appropriate here because all the objects have certain shared features like collision properties,
	movement properties, or drawing properties. In addition, subtyping is needed because all objects in the game grid need to have 
	the same supertype in order to put them into a 2D array.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  1. GameSprite.java
  	 This is the base abstract class that represents a sprite (object) in the game. A GameSprite is a rectangle object 
  	 that has implemented setters and getters for width, height, xPosition, and yPosition. It also has an unimplemented
  	 draw() method that its subclasses implement. 
  		2. EmptySprite.java
  		   EmptySprite is a subclass that extends GameSprite.java. It has the same functionality as a GameSprite, and it
  		   implements the draw() method to draw a transparent rectangle. EmptySprite is used in the BlockGrid class to 
  		   represent an empty space.
 		3. PlayerBallSprite.java
 		   PlayerBallSprite is a subclass that extends GameSprite.java, implementing the draw() method. It draws a white 
 		   circle to represent a ball. It also adds a move() method that sets the x and y velocities of the ball. It also 
 		   adds a bounce() method to allow the ball to change its direction. It has various collision methods to determine 
 		   if the ball has collided with another sprite and what the direction of collision was. It also has a launchReady and
 		   isActive state to check if the ball is ready for launch and if the ball is currently moving, respectively.
 		4. CollisionSprite.java
 		   CollisionSprite is an abstract subclass that extends GameSprite, representing a GameSprite that can collide with 
 		   other objects. It adds an additional onCollision() unimplemented method as well a readyToBeDestroyed state that changes 
 		   based on collision.
 			5. CoinSprite.java
 			   CoinSprite is a CollisionSprite that implements the onCollision() and draw() method. It draws a white circle to 
 			   represent a coin. When the CoinSprite gets collided with, the readyToBeDestroyed state is set to true
 			6. NumBlockSprite.java		
 			   NumBlockSprite is a CollisionSprite that implements the onCollision() and draw() method in different ways. It draws
 			   a rectangular block with a number in the center to represent the block's health. The number is chosen based on the 
 			   player's current score. A higher score means the block will have a higher health. On collision, the health state
 			   decreases by 1. When the health reaches 0, the readyToBeDestroyed state is changed to true.
  7. BlockGrid.java
  	 BlockGrid.java is the class that holds the game grid, represented by a 2D array of GameSprites. BlockGrid extends JComponent, 
  	 overriding the paintComponent() method to paint the 2D array of GameSprites. This class has methods to initialize the grid 
  	 at the beginning of the game, update the grid on each round, check if the grid has reached game-over status, and increment the 
  	 score on each round.
  8. PreviewLine.java
  	 PreviewLine.java is a JComponent that paints the preview trajectory line that is seen as the mouse is moved around the game
  	 region. It contains setters and getters for the x1, y1, x2, and y2 positions of the line.
  9. PlayerBallCounterSprite.java
  	 PlayerBallCounterSprite is a class that draws a label containing the number of balls that the player currently has. It has a 
  	 draw() method to draw the label.
  10.PlayingArea.java 
  	 PlayingArea is a JPanel that houses the entire game region. It stores a LinkedList of PlayerBallSprites which keeps track 
  	 of how many balls a player has. It paints the BlockGrid, PreviewLine, PlayerBallSprites, and PlayerBallCounterSprites. 
  	 It also contains listeners to check for mouse movement to redraw the PreviewLine and for a mouse click to set the release 
  	 of the PlayerBallSprites. This class uses a Timer to continuously redraw the game court to move the balls, handle collisions, 
  	 increment score, and check for game over. It also uses another Timer to set a delay between the launch of each subsequent 
  	 PlayerBallSprite in the  LinkedList.
  11.Game.java
  	 The Game class contains a main method to run the game. It implements Runnable and has a run() method where the frame of the 
  	 game is setup. The layouts and formatting of the score label, menu buttons, and game-region are all setup here. The listeners
  	 for the button clicks are also setup here to display the corresponding information in JOptionPane message dialogs.
  12.HighScoreReader.java
  	 HighScoreReader is a class that is used to read lines in the highscores txt file. It converts a line in the txt file
  	 to a Scorer object and it implements Iterator to allow you to iterate through all the Scorer objects.
  13.HighScoreParser.java
  	 HighScoreParser is a class that contains a TreeSet of Scorer objects to keep track of the top 3 highscores. The class
  	 can read from the highscores txt file using HighScoreReader or write to the txt file using BufferedWriter. It can also
  	 write a highscore to the txt file with an inputed Scorer object. The score only gets added if it's in the top 3
  	 highscores.
  14.Direction.java
  	 Direction is an enum with values UP, DOWN, LEFT, RIGHT. It is used to determine the direction a ball should travel 
  	 in the PlayerBallSprite class.
  15.ColorPalate.java 
  	 ColorPalate contains a static list of colors that are used throughout the game. It also contains a randomColor() method
  	 that returns a random color from the palate. The ColorPalate is used to color the NumBlockSprites, the background of 
  	 the JPanels, and the menu buttons.
  16.Scorer.java
  	 Scorer is a class/object that keeps track of all the highscore information of a particular user. It stores the 
  	 first-name, last-name, pennkey, and score of the player. It is used for parsing in both the HighScoreReader and
  	 HighScoreParser classes.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  The only significant stumble I had when implementing my game was figuring out how to create a small time-delay in between 
  the launch of each of the balls in the list of balls. In order to figure this out, I had to modify the design of my PlayerBallSprite
  class to include a boolean launchReady state. The launchReady state keeps track of whether the ball is ready for launch. It
  is initially false. To launch the balls with a small delay after each launch, I use a timer to set the launchReady state
  of each ball to true every 100 milliseconds.
  
- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  
  I think there is good separation of functionality in my design. My design follows
  the MVC design pattern best it can, separating the view from the controller from 
  the model. Private state is encapsulated where it should be. If given the change,
  I would refactor the PlayingArea class to further separate the functionality in it. 
  I would possibly separate the timer functionalities and the listener functionalities 
  into separate classes so that there is better separation in the design.

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  None.
