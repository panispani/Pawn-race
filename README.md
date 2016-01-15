# Pawn-race

Pawn Race is a chess-like game played on a normal 8x8 chess board.
Each player has 7 pawns.
White pawns are placed on the 2nd rank and Black pawns on the 7th rank.
Each player has a gap as there are 8 files but only 7 pawns.
The gaps of each player are decided by the Black Player.
This is an attempt to implement this game and also a Computer player using the Minimax algorithm with Alpha-Beta pruning.
The Computer player replies within 5 seconds.

This is an example setup with white gap on the A file and black gap on the G file.
```sh
   A B C D E F G H

8  . . . . . . . .  8
7  ♟ ♟ ♟ ♟ ♟ ♟ . ♟  7
6  . . . . . . . .  6
5  . . . . . . . .  5
4  . . . . . . . .  4
3  . . . . . . . .  3
2  . ♙ ♙ ♙ ♙ ♙ ♙ ♙  2
1  . . . . . . . .  1

   A B C D E F G H
``` 
   
# HOW TO RUN


You can run the ready .class file by switching to the out/production/Pawn-race/ directory and running the PawnRace class

>`cd out/production/Pawn-race/`

>`java PawnRace`


# HOW TO PLAY


The pawns promote and capture the normal way(enPassant is allowed).
The first player to promote a pawn wins(or the one who captures all opponent's pawns).
When a player is about to play but has no legal moves the game is considered a stealmate(draw).
The game accepts moves in either Short algebraic notation or Standard Algebraic Notation(stating both start and end square of pawn movement)
See: https://en.wikipedia.org/wiki/Algebraic_notation_(chess)
Pawn capturing is denoted by 'x'


E.g. Example game on the above board
(You can try it to understand the notation, just choose Human Vs Human(H H) at start)

Short Algebraic notation
```sh
1.  f4 a5
2.  g4 b5
3.  g5 f5
4.  gxf6 exf6
5.  e4 a4
6.  c3 c5
7.  e5 fxe5
8.  f5 b4
9.  f6 a3
10. f7 a2
11. f8
```

Standard Algebraic notation
```sh
1.  f2-f4 a7-a5
2.  g2-g4 b7-b5
3.  g4-g5 f7-f5
4.  g5xf6 e7xf6 (En Passant rule)
5.  e2-e4 a5-a4
6.  c2-c3 c7-c5
7.  e4-e5 f6xe5
8.  f4-f5 b5-b4
9.  f5-f6 a4-a3
10. f6-f7 a3-a2
11. f7-f8
```


Final Position
```sh
   A B C D E F G H

8  . . . . . ♙ . .  8
7  . . . ♟ . . . ♟  7
6  . . . . . . . .  6
5  . . ♟ . ♟ . . .  5
4  . ♟ . . . . . .  4
3  . . ♙ . . . . .  3
2  ♟ ♙ . ♙ . . . ♙  2
1  . . . . . . . .  1

   A B C D E F G H
```
WHITE HAS WON!!

TIP: Black has a slight advantage.
This was done for the Imperial College London Pawn Race AI challenge.
Enjoy!

