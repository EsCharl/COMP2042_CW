# COMP2042_CW_hcycl3
 
## Introduction

This github is used for Software Maintenance Course Work which is tasked to refactor and maintain the video game from <a href="https://github.com/FilippoRanza/Brick_Destroy"> Brick Destroy</a> by FilippoRanza.

## Changes Made

The changes made on this software is refactoring the software and including design patterns into this software (namely singleton and factory pattern). I've also included MVC pattern for this software. these activities will allow the software to be more expandable in the future. the refactoring includes removing dead code (and change some to be allowed to use for different scenarios and achieve the same thing), correcting some of the game logic, improving the ball speed when it is started, changing of variable and method name to make it more understandable. for readability of the code, I've also split up the classes to multiple classes to make it easier to understand and split them based on different responsibility. I’ve also encapsulated all fields which will allow future user/developer to be able to extend/improve the software much easier.

As for additions, I’ve included 2 new levels and a new brick called “Reinforced Steel Brick” this brick has a 30% probability of getting damaged and with 2 hit points. The levels would have a random level which will randomly generate a wall every time. And a level called “Two Lines” which have 2 lines going somewhat vertical on the left and right side of the wall and have the new brick and the steel brick as the bricks for the wall. and include some aesthetic changes on the debug panel. which includes the value on the debug panel and indicator on which slider is for which axis speed. I’ve also added an info button on the home menu that contains the information on how to player the game, and an image on the home menu to make it more aesthetically pleasing. As a game will be more interesting with a scoring system. I’ve created a timer system from the ground up that uses the system time to calculate the time needed to complete the level. This will allow the user to compete with himself (or with others) on the time needed to complete the level. There will be a score list pop up displayed on the screen after each level which allow the user to determine on his performance. The score will be saved in a text file after completion of a level for future reference. 

As for increasing the fun factor of the game I implemented an additional feature, there will be a probability that the ball will speed up or slow down when it hit the game window or the paddle.
