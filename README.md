# COMP2042_CW_hcycl3
 
##Table of Contents 

1. [Introduction](#introduction)
2. [Changes Made](#changes-made)

## Introduction

This github is used for Software Maintenance Course Work which is tasked to refactor and maintain the video game from <a href="https://github.com/FilippoRanza/Brick_Destroy"> Brick Destroy</a> by FilippoRanza.

## Changes Made

The changes made on this software is an addition the the Info on how to play the game.

![image](https://user-images.githubusercontent.com/63916811/141622130-778e708b-da2e-4ad8-85d4-e9876ba01cf7.png)

An addition of Integer Values and what are the values for.

![image](https://user-images.githubusercontent.com/63916811/141623211-44a92320-45b0-445a-b380-50ced00b74cb.png)

A pause menu is activated when the user enters debug console.

![image](https://user-images.githubusercontent.com/63916811/141623835-33633092-678f-4702-b01f-2b537044d13b.png)

An additional level is also added. this level will see an introduction of a new brick called "Reinforced Steel Brick" this brick have a 50% chance of getting damaged and it have a strength of 2.

![image](https://user-images.githubusercontent.com/63916811/141646176-f35d03c8-e490-4851-b3e9-e5ddf477b819.png)

And a highscore system based on time. 

![image](https://user-images.githubusercontent.com/63916811/141625728-c051a5a6-25ba-495e-97b7-4e655fd034a8.png)

The scores will be stored in a .txt file.

The game source code have also undergo some changes.

![image](https://user-images.githubusercontent.com/63916811/142233279-ad0bc61b-8448-4157-90f6-b5bc971239e5.png)

some of the if-else statements have been changed to if statements as if the ball have reached the top corners it might be going out of the screen. (Changes in Wall.java)

![image](https://user-images.githubusercontent.com/63916811/142235289-a32f3446-5813-464e-bd6b-7b670ffc0b4e.png)

there is also an issue for this line of code in the Brick.java class as the method "inMiddle" have the parameters in the order of int i, int steps, int divisions.

![image](https://user-images.githubusercontent.com/63916811/142235868-d7f14275-f953-424d-a6ec-70916421b370.png)
