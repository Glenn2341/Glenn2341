The 8-puzzle consists of a 3x3 board onto which nine tiles are placed, each labeled with a unique number. One tile is considered to be a ‘blank’ space into which adjacent tiles can be shifted. The game is ‘won’ when the tiles have all been arranged into ascending order, with the ‘blank’ tile located in the upper left corner. 

I used an A* algorithm to navigate this state space without having to search every possibility. The A* algorithm ranks available moves based on their distance from the root and an added ‘heuristic’ value, which takes into account the distance of every piece on the board from its location in the goal state.

Each time a move is made, a unique hash signature of the current board-state is generated and stored in a Hash Set, and this is used to make sure there is never a move made that would take the board back to a previously explored state. This technique is called memoization, which basically means keeping track of previous moves to avoid re-making them. 
