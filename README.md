# COMP2042_CW_hcycl3
 
## Introduction

This github is used for Software Maintenance Coursework which is tasked to refactor and maintain the video game from <a href="https://github.com/FilippoRanza/Brick_Destroy"> Brick Destroy</a> by FilippoRanza.

## Changes Made

As this Coursework, is mainly tasked on refactoring and maintain the software, I've retain most of the logic from the base version of the software. The changes includes:

- removal of the Crack Class and moved it's logic to the GameStateController.
- removal of datatypes that uses AWT or Swing in favour for JavaFX counter part.
- changing the collision position of the ball to more realistic scenario. (taking the direction of the ball for collision instead of the center of the ball).
- removal of some redundant methods from the base version of the crack class.
- simplifying the methods that are being overrided from brick that suits different bricks to a single method.
- removal of one level template called SingleTypeLevel and replace it with other wall level template which takes in two same brick type to achieve the same goal.

+ changing the whole program to adhere to MVC and some other design patterns (namely singleton and Factory Pattern).
+ renaming the methods to more suit on what they do.
+ inclusive of javadocs for all class and methods.
+ switching the software from swing and AWT fully to JavaFX.
+ addition of audio and sound effects for the game. (game tunes for the progress of the game. sound effects for winning/losing and the collision between the ball and other entities).
+ addition of a new brick called Reinforced Steel Brick which will have a chance of a hit based on a 30% probabilty and with a strength of 2.
+ addition of three new level called RandomWallLevel which will be random generating a level with all the different bricks and another level is called CurlyLinesWallLevel which will generate a level with only curly brick line and lastly, StraightLinesLevel which will generate a wall level with only straight brick vertically to represent lines.
+ creating a new interface called moveable for ball and player, and crackable for some brick classes.
+ added a feature where everytime the ball collides with an entity or the side of the gamewindow it will increase or decrease the ball speed by 1 or even maintain the speed just reverse.
+ addition of a scoring system based on time which will allow the user to compete with itself on it's performance on every level. (GameScore class)
+ addition of the scoreboard popup after every successful completion of the level. (GameScoreDisplay class)
+ addition of a score saving feature to a text file after every successful completion of level. (GameScore class)
+ a little aesthetic changes on the debug console and allow the user to switch between level.
+ added images for the background (main menu and gameplay) and game icon.
