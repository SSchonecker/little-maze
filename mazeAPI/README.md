# Little Maze Domain API

This is where the domain logic is turned into the information for the visuals of the Little Maze-project.

There are two main classes (MazeInitiate and MazePlay), and four classes to handle data-transfer. <br/>
The MazeInitiate class handles POST requests to start the game, MazePlay handles PUT requests during the game. The POST request should have the format of the PlayerInput class, i.e. contain the fields _playerName_ and _gridSize_. Both request handler classes return a Maze object, which contains a _player_ object, a _gameStatus_ object and a _layout_ matrix of strings. The Player object has the player information consisting of _name_, _health_, _steps_ and their _position_. Finally, the GameStatus object has information on the _endgame_ and the _score_.

## Output

- A server listening on port 2222
- A grid object sustained for a session (note that this will be gone after a while)

## Input

- The Grid class from the domain and all its information

## Usage

Just run the `../runAPIserver.bash` file from the project's main folder. It will install the domain jar and then start the server, installing all dependencies in the API folder.<br/>

To start a new game, a POST request with the name of the player and the required grid size should be sent to 2222/littlemaze/api/start, which will return the maze layout and player information.<br/>
To make a move or turn (=stir), a PUT request with the key used for the move is to be sent to 2222/littlemaze/api/stir/{key}, where the {key} parameter will be used to make a move on the grid object of this session. **Note that this is only possible as long as the session exists**
A start of the handling of the selection of a tile is also included.
