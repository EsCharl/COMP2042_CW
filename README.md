# COMP2042_CW_hcycl3
 
## Introduction

This readme is used for Software Maintenance Coursework which is tasked to refactor and maintain the video game from <a href="https://github.com/FilippoRanza/Brick_Destroy"> Brick Destroy</a> by FilippoRanza.

## Changes Made

###_Refactoring_

- changing the collision position of the ball to fix an issue of the ball glitching out. (taking the direction of the ball for collision instead of the center of the ball).
- refactoring the methods or replace methods that could achieve the same result. (setImpact (SteelBrick, CementBrick), removal of makeSingleTypeLevel (Wall))
- extract the crack class out from the brick class and removal of some redundant methods from the base version of the crack class. (inMiddle, jumps)
- creation of multiple interface (crackable, Movable) to adhere to interface segregation principle, and abstractions (FullWallRowsLevel) for Open-Closed Principle.
- implementation of factory pattern (BrickFactory, LevelFactory) and split into multiple classes for different level templates to allow Single Responsibility Principle.
- included singleton design pattern on the GameData (Wall in the base ver.), Player and GameScore as they only used for logging activities.
- renaming the methods to more suit on what they do.
- inclusive of javadocs for all class and methods for more detail explanation on what it does.
- organized the code to adhere to MVC structure.
- self encapsulation for all variables. (for future expandability).
- ensured all classes adhered to Single Responsibility Principle.
- split the methods in classes for smaller class size.
- changed the issue where the ball move fast during the start of game.

###_Additions_

+ addition of audio and sound effects for the game.
+ addition of a new brick called Reinforced Steel Brick which will have a chance of a hit based on a 30% probability and with a strength of 2.
+ addition of a scoring system based on time which will allow the user to compete with itself on its performance on every level. (GameScore class)
+ addition of the scoreboard popup after every successful completion of the level. (GameScoreDisplay class)
+ addition of a score saving feature to a text file after every successful completion of level. (GameScore class)
+ aesthetic changes on the debug console and allow the user to change level.
+ added images for the background (main menu, gameplay) and game icon.
+ addition of three new level called RandomWallLevel, CurlyLinesWallLevel, StraightLinesLevel.
+ added a feature where everytime the ball collides with an entity or the side of the game window it will increase or decrease the ball speed by 1 or even maintain the speed in an opposite direction.
+ added a feature where a bot will play the game for the user.

###_JavaFX_

* revamped the software to fully using JavaFX 
* removal of libraries that uses AWT and Swing in favour for JavaFX counterpart.
* added CSS to style on the text and buttons to mimic the base version of the game.
* produced multiple fxml files for the View and Controllers for those fxml.