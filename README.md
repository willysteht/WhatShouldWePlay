# WhatShouldWePlay
I wrote this App to learn a bit about google sheets API. If you need help with what to play (or what to do) with your friends this App can help.

Be aware that you need a google developer project with the sheets API enabled to fully work with this project.
https://console.developers.google.com

You can build and start the project without doing anything. It will crash the first two times because you need to enter your credentials and the spreadsheet id. Check the logfile (all.log) for further information. (sounds complicated but is easy)

You obviously need a google spreadsheet. You can use ours as a reference (File -> Make a copy... to create your own). https://docs.google.com/spreadsheets/d/1uDXxp_RWMi17fI54keOLQDMYeVaRmRdc1lWDTxaEmu0

In Column A you find the activities, games, whatever you or your friends want to play. Change them as you want.

In Column B to D every person writes numbers. You can define your own range (ours is from 0 to 1). This Number represents a weight how likely the person wants to play the game from column A. (be carefull with cheaters here :P) If you are alone, or two people, just delete the unnecessary entries.

Column E is emty. (who could have thought)

In Column F to H are some calculations. Column G shows how likely it is, that this row will be the winner. The sum of column G must be 1!

#How it works

Basically the App throws a dice with 10000 sides. The game that is in range of the thrown number gets a point. First one that has 3 points is the winner.

The App uses Column A, H and I.

From A it only reads the names.

From H it reads the values, throws a dice and if the rolled number is <= the value of H the game gets the point.

The points are written to column I. Points are represented as Pipes("|"). First row with three pipes wins. Game Ends.
