# Tetris
## Java Tetris with hard-coded AI

In Test.java, there are three functions which represent the game modes. These are:
* **humanPlayer:** Singleplayer  
* **AI:** Watch the computer play by itself  
* **both:** Player plays against the computer

How to play:
* Left and right arrow keys to move the piece left or right
* Up arrow key rotates the piece.
* Down arrow key accelerates the freefall.
* The speed of the game increases as time passes. In order to restart the game, the code should be run again.

If a certain type of Tetris piece is generated too frequently, then new pieces are generated. 

In the AI part, the computer evaluates the pieces according to its type. The top surface of the piled pieces is checked for a suitable place. Then the piece is turned and horizontally shifted if necessary to align to that place. To give an example, let's say that the piece is a square. First, the AI searches for two adjacent tiles on the same level, also in the lowest altitude as possible. If there is no such place, then the program searches for less efficient alternatives. For all types of pieces, a similar procedure applies.
