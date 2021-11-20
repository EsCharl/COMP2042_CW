# COMP2042_CW_hcycl3
 
Table of Contents 

1. [Introduction](#introduction)
2. [Changes Made](#changes-made)

## Introduction

This github is used for Software Maintenance Course Work which is tasked to refactor and maintain the video game from <a href="https://github.com/FilippoRanza/Brick_Destroy"> Brick Destroy</a> by FilippoRanza.

## Changes Made

The changes made on this software is an addition the the Info on how to play the game.

![image](https://user-images.githubusercontent.com/63916811/141622130-778e708b-da2e-4ad8-85d4-e9876ba01cf7.png)

An addition of Integer values on the slider and what are the values for.

![image](https://user-images.githubusercontent.com/63916811/141623211-44a92320-45b0-445a-b380-50ced00b74cb.png)

A pause menu is activated when the user enters debug console.

![image](https://user-images.githubusercontent.com/63916811/141623835-33633092-678f-4702-b01f-2b537044d13b.png)

An additional level template is also added. this level will see an introduction of a new brick called "Reinforced Steel Brick" this brick have a 30% chance of getting damaged and it have a strength of 2.

![image](https://user-images.githubusercontent.com/63916811/141646176-f35d03c8-e490-4851-b3e9-e5ddf477b819.png)

And a highscore system based on time. 

![image](https://user-images.githubusercontent.com/63916811/141625728-c051a5a6-25ba-495e-97b7-4e655fd034a8.png)

The scores will be stored in a .txt file.

The game source code have also undergo some changes.

![image](https://user-images.githubusercontent.com/63916811/142233279-ad0bc61b-8448-4157-90f6-b5bc971239e5.png)

Prevents the user from expanding the screen, the reason is mainly because it will enable the user to be focus on a smaller screen window instead of a big window.

![image](https://user-images.githubusercontent.com/63916811/142454723-a96282d8-9119-4717-be47-43032a5a6c59.png)

some of the if-else statements have been changed to if statements as if the ball have reached the top corners it might be going out of the screen. (Changes in Wall.java)

![image](https://user-images.githubusercontent.com/63916811/142235289-a32f3446-5813-464e-bd6b-7b670ffc0b4e.png)

there is also an issue for this line of code in the Brick.java class as the method "inMiddle" have the parameters in the order of int i, int steps, int divisions.

![image](https://user-images.githubusercontent.com/63916811/142235868-d7f14275-f953-424d-a6ec-70916421b370.png)

I've also changed the variable name of radius to diameter to more suit the naming and functionn of the parameters. The change is mainly due to the method divided the variable by half during the process of the method and also the Point2D.double variables are being set location using the variables being halved.

![image](https://user-images.githubusercontent.com/63916811/142373987-f446429c-53e2-4ad6-9039-0521889a7519.png)
![image](https://user-images.githubusercontent.com/63916811/142374051-27e66722-b1d9-4a47-a6c4-764f57689456.png)

these methods could be removed from Steel Brick class as it is redundant.

![image](https://user-images.githubusercontent.com/63916811/142449884-fa66fadf-0f36-45b2-ac0c-b4b1438e3948.png)

This method which is present in the Clay Brick, Steel Brick and Cement Brick is redundant as the super class (Brick) already have the method and serves the same purpose. thus, redundant.

![image](https://user-images.githubusercontent.com/63916811/142450152-347cb7ab-79ed-4665-8037-7e16e43aafb0.png)

I've changed the requirements to be on another method for ease of changing/addition of new buttons in the future.

![image](https://user-images.githubusercontent.com/63916811/142560183-6ff4ec2f-4b6e-4338-8c8e-3bd36d6f0b05.png)

changing of direct assignment to method calling. (Ball)

![image](https://user-images.githubusercontent.com/63916811/142561952-0558522d-7ad5-4866-b43a-2a361e9e47bf.png)

this is used to set the variables

![image](https://user-images.githubusercontent.com/63916811/142562488-dacc1a46-f015-4614-b744-ab64733ec913.png)

this it to create the direction points of the ball. this can be expanded in the future to include more directions.

![image](https://user-images.githubusercontent.com/63916811/142565180-8b9e8c9c-3de6-40ce-843a-ea047736e47f.png)

A refactoring in Ball class. (removal of some code because there is a method that serves the same function)

![image](https://user-images.githubusercontent.com/63916811/142565932-ea46b5e3-f64b-4b3f-a4c3-380e351bd213.png)

change of method name (SetPoints -> setDirectionalPoint) to make it more precise.

![image](https://user-images.githubusercontent.com/63916811/142566472-f3f0f2f2-347c-4b94-926c-87ebb8234fbc.png)
