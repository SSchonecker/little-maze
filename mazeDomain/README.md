# Little Maze Domain

This is where the logic of the Little Maze-project is constructed. There are three main classes (Tile, Player, Grid), one subclass (Spike) and one helper enum class (Direction). <br/>
The idea is that there is a continuous interaction between the Player and the floor Tile they are standing on. Each Tile knows about its neighbours and passes the player along as they move. On constructing the first tile, the entire maze is build through a depth-first search. Certain tiles are Spike tiles, which means that they will hurt the player when they step on it. One tile contains a chest, that nudges the player to set the end of the game.
<br/>
The Player contains all the information on health, steps taken, current position and orientation, and whether the game is still going on. <br/>
The Grid holds the information of the game state and will be used to create the game from the API-layer. It handles output and input.

## Output

- A maze consisting of the wall configurations of each tile in the grid
- The location of the chest
- Player information: name, health, steps taken, score, game over, position, orientation

## Input

- A stirring of the Player (move forward, backward or turn left, right) entered through the keys _w, a, s, d_
- The selection of a tile, to see whether it is spikey or contains a chest

## Usage

Install the program with
	mvn clean install

Run tests with
	mvn clean verify
This ensures the creation of jacoco-code coverage reports.

## Depth-first search

Depth-first search is an algorithm that builds a perfect maze by treating the paths in the maze as branches of a tree. A maze is a 2-dimensional grid with walls and connected floor tiles or cells. A perfect maze contains no loops and no empty or unconnected cells. Instead, it has one main road from beginning to end and several splits leading to dead ends along the way. These branches from the main path make the maze comparable to a tree and the 2-d grid to a graph. <br/>
The depth-first search algorithm starts at one point on the grid (here on (_0, 0_)) and picks a random neighbouring cell that hasn't been visited before. It connects these two cells and then repeats this process on the neighbour. If all cells surrounding the current cell have been visited, the algorithm moves back along the branch to a point where a cell has unvisited neighbours. It ends once all cells have been visited.

For testing reasons, the first neighbour is taken to be to the EAST of the first tile. This ensures that there is only one possible 2x2 maze and that the player's orientation at the start of the game is set correctly.
test maze	+--+--+
			| c  s|  c = chest, s = spikey tile
			+--+  |
				  |
			+--+--+
