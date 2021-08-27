# Breakout Game by sudhir (jpp20)

This program is a basic rendition of the classic breakout game with an added multiplayer.
    Date Started : 05/07/2021
    Date Finished: 27/08/2021
    Time Spent: ~20 hours

# Resources used
My BouncerObject class is very heavily based off of Bouncer.java by Robert Duvall found at https://coursework.cs.duke.edu/CompSci308_2017Fall/lab_bounce

I used JavaDocs and Piazza as resources to help me with this project

    Image Sources were 
    - http://johnmarinedesigns.weebly.com-
    - https://www.123rf.com/stock-photo/tennis_ball_cartoon.html
    - https://www.mariowiki.com/Mushroom

# Running the program
The main method is the GameRunner.java class. 
# Features
The games biggest feature is the addition of multiplayer mode. Multiplayer mode is very similar to vertical pong, but with blocks in the middle. The goal of the game is not to break as many blocks as you can, but to have three balls pass your opponent before three ball bass your paddle. Both single player and multiplayer consist of 10 unique levels with 10 unique blocks. Every level the row of blocks with the highest lives is equal to the current level. Blocks are generated algorithmically. Additionally, in single player various powerups have a random chance to drop on the breaking of a brick. The powerups last for a limited amount of time. Additionally, using cheat codes players can activate three different types of paddles. The various powerups and paddle types are below.

    Power Ups: 
    - Plus one life
    - Minus one life
    - Score multiplier
    - Large Paddle
    - Small Paddle
    - Fast ball
    - Slow Ball
    
     Types of Paddles: 
    - Normal
    - Super Bouncy (ball bounces more off it each time)
    - Sticky (ball bounces less each time it bounces off the paddle)
    
    Other Cheat Codes:
    - Increment Level
    - Give additional life
    - Pause bouncers
    - End game
    
# Known issues
When a ball hits two blocks at once, due to the way the bounce algorithm is implemented, the block will essentially be completely destroyed regardless of how many lives the block has. This is because the direction is reversed twice because there is two blocks leading to the direction to in essence not reverse. This problem could have been fixed with a more complex bounce algorithm though that would have had some tradeoffs.

#Impressions
Generally, I enjoyed the project. I think I learned ways to improve my code on wednesday in class when I had already started, so I think knowing those things now I would have perhaps structured my code differently. I think the biggest take away for me in this project is the importance of planning. I think if I had taken more time to plan out the architecture I would have benefited. 