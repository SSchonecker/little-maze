# Little Maze Front End

This is where the visuals of the Little Maze-project are constructed. <br/>
The package and package-lock files contain the project's information for node.js, while the tsconfig file holds the setup for typescript compilation. The file webpack.config bundles the script files, but needs the public folder accessible. It also contains the information on redirecting calls to littlemaze/api/, so should one change the port number of the API server, it should also be adjusted here.
The public folder contains the main HTML file, images and the CSS-file. <br/>
The src folder holds the react-scripts responsible for the creation of the webpages. startgame.tsx is the main file connecting to the index.html and rendering the App. The App creates either the initialize game/main page or the game/play page. The construction of these pages happens in the InitGame and Play classes. GameState.ts only contains interfaces for typescript objects used.

## Output

- A server listening on port 2200
- A start page to get a player's name and a maze size
- The actual maze for the player to get lost in

## Input

- POST and PUT requests to port 2222/littlemaze/api/...

## Usage

Just run the `../runFEserver.bash` file from the project's main folder. It will install the necessary node.js dependencies and start the server.

## A note on the grid lay-out

The grid in the domain was depicted as
			+--+--+
			| c  s|  c = chest, s = spikey tile
			+--+  |
				  |
			+--+--+
Due to how the grid-items are generated, the actual maze on the website is rotated by +90 degrees:
			+  +--+
			|  | c|  c = chest, s = spikey tile
			+  +  |
			|	 s|
			+--+--+
This discrepancy is most visible in the generation of the walls in Play.tsx: the lay-out matrix element is of the form {tile-type, north border, east border, south border, west border}, but the north border determines the placing of a wall or border on the right side of a tile, the east border on the bottom etc.
