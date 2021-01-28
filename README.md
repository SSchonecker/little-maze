# Little Maze

The goal of this project is to make a mini dungeon crawler. The domain creates the maze and contains the Player and floor Tile interaction rules. These are, for now:
 - The Player can walk over tiles, but not through walls
 - If the Player steps on a spikey tile, they lose health
 - If the Player steps on a tile with a chest, the game ends and his score is set with Score = Treasure in chest - Steps taken + Health left

