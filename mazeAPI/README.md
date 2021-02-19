# Little Maze Domain API

This is where the domain logic is turned into the information for the visuals of the Little Maze-project.

There are five main classes (MazeLogin, MazeInitiate, MazePlay, MazeSave, and MazeEnd), eight classes to handle data-transfer, and three classes to deal with the connection with the database. <br/>
The MazeLogin class handles POST requests to log in into the game site. These requests should contain a json with the fields _userName_, _password_, and _createAccount_, which indicates whether a new account should be created. The password is hashed and either stored in the database or compared to the stored hash from the database. Either way, a json is returned containing the userName and an accessToken that confirms on all further requests that the user is correctly logged in. <br/>
The MazeInitiate class handles POST requests to start the game, or PUT requests to load a game. To initiate a new game, the POST request to 2222/littlemaze/api/start should have the format of the PlayerInput class, i.e. contain the fields _playerName_ and _gridSize_. To load a game, a PUT request should be sent to /start/load/{slot-nr} to load from slot 1 or 2. <br/>
MazePlay handles PUT requests during the game. The possible actions are a player move to 2222/littlemaze/api/stir/{key}, with key the selected key of the player, or a tile selection to /stir/tile/{position-string}.
These request handler classes (Initiate and Play) all return a Maze object, which contains a _player_ object, a _gameStatus_ object and a _layout_ matrix of strings. The Player object has the player information consisting of _name_, _health_, _steps_ and their _position_. Finally, the GameStatus object has information on the _endgame_ and the _score_. <br/>
The MazeSave class passes on the session's grid information to one of two save slots, based on the PUT request to 2222/littlemaze/api/save/{slot}. It does not return anything other than the response status (if everything goes well).<br/>
The MazeEnd class responds to PUT requests to 2222/littlemaze/api/end/{score} with the result of handling a new score in SQLConnect. The new score is compared to previously saved scores and either added to the database or ignored, if it is not high enough. Only the five highest score per user are stored. The response is a list of score-row objects, each containing a _scoreID_, _scorevalue_, _gridSize_, _datetime_ and _userID_.

## Output

- A server listening on port 2222
- User access verification
- A grid object sustained for a session (note that this will be gone after a while)

## Input

- The Grid class from the domain and all its information

## Usage

Just run the `../runAPIserver.bash` file from the project's main folder. It will install the domain jar and then start the server, installing all dependencies in the API folder.

To change the port number, the defaults in src/main/resources/jetty.xml and src/main/resources/jetty-https.xml need to be adjusted, as well as the proxy port of the webpack-configuration file of the FE server. <br/>
To change the outgoing port number for the database, change the urls passed to SQLConnect in the five main MazeXxx-classes.

To login, a POST request with a userName and password should be sent to 2222/littlemaze/api/login, which will return an access token required for further communication with the server.
To start a new game, a POST request with the name of the player and the required grid size should be sent to 2222/littlemaze/api/start, which will return the maze layout and player information.<br/>
To make a move or turn (=stir), a PUT request with the key used for the move is to be sent to 2222/littlemaze/api/stir/{key}, where the {key} parameter will be used to make a move on the grid object of this session. 
Tile selection should be sent to 2222/littlemaze/api/stir/tile/{position-string} with position-string being "_xy_" for a set of x- and y-coordinates. **Note that this is only possible as long as the session exists.**

## On HTTPS
HTTPS is an encrypted communication protocol between a server and a client. It ensures confidentiality, because the transvered message can't easily be decrypted, integrity, because each side can check if all content has been transmitted, and authentication through signed certificates.
Communication starts with asymmetric encryption: the first response from the server is its public key, which the client uses to encrypt a secret. The server then decrypts the secret with its private key and from thereon all communication is symmetrically encrypted. Authentication comes in when the server's public certificate has been acknowledged by a certification agency.
The client then first recieves the public key and a CA certificate with which it can check at the CA whether this server really is what it claims to be. This means that the CA needs to be trusted. Without the CA certificate, the keys are said to be self-signed. In this case any server can claim to be the server the client is trying to connect to, so most browser would give a warning about an untrusted certificate in this case.

Most server applications can be configured to handle HTTPS requests automatically, provided with certificates or key-sets. Usually it is also possible to set up an automatic rerouting from a call on HTTP to the corresponding HTTPS port.

However, this server only listens to HTTPS requests. The server setup is mostly determined by the jetty configuration files in /src/main/resources/ (most helpful resource: https://stackoverflow.com/questions/3794892/howto-use-https-ssl-with-maven-mortbay-jetty-plugin/3795116#3795116).
For the SSL-certificates/key-pairs, the keytool generator is used automatically during the build of the project through maven. This means that on each build a new pair of keys is generated. These are self-signed keys, so they are not certifite by an CA, but webpack-devserver (the FE server) knows how to ignore this fact.

## On password hashing
Password hashing is the process of encrypting a password in such a way that the computational cost of encrypting it is high and it is borderline-impossibly hard to decrypt it. The high cost ensures that to a hacker it is not worth it to randomly try to find either the initial password or the used hashing function by hashing many passwords and comparing the hashes.
A hashing function or key derivation function produces a key from a secret value (the password), usually with a fixed length. To secure the hashing of passwords more, a salt can be added to each password before hashing. The salt is a unique random string which is stored visibly with the hashed password. This can prevent the use of rainbow tables, which are pre-set tables containing common passwords and the output of several hashing functions to determine the used hashing function.

bcrypt, the hashing function used here, also creates a salted password hash, using the blowfish cipher (which seems somewhat complicated...). It encrypts the password into a hash of the format 
<br/>
$2a$[cost]$[22 character salt][31 character hash]
<br/>
where the first part indicates what type of hash and what type of format is used. The cost number indicates the exponent used for the number of key expansion rounds. Key expansion involves taking the initial key and using it to come up with a series of other keys for each round of the encryption process. The more rounds, the more expensive the hashing function and thus again more secure. <br/>
On logging in, the stored password hash is compared by bcrypt to the newly entered password.
