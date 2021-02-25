# Little Maze

The goal of this project is to make a mini dungeon crawler.

The domain creates the maze and contains the Player and floor Tile interaction rules. These are, for now:
 - The Player can walk over tiles, but not through walls
 - If the Player steps on a spikey tile, they lose health
 - If the Player steps on a tile with a chest, the game ends and his score is set with Score = Treasure in chest - Steps taken + Health left

The API combines the domain and the database and sends this information to the front-end. During a normal session, it gets the login credentials and returns an access token, creates a Grid object and handles game state updates.

The front-end or FE creates the website for a user to play on.

Additional and detailed information can be found in each of the sub-folders.

Provided a correct database, the entire app should start with the /runXXserver.bash files and the site can then be found at https://localhost:2200/. <br/>
The ports used are 2200 for the FE server, 2222 for the API server and 4444 for the docker-based database.
