# Little Maze Front End

This is where the visuals of the Little Maze-project are constructed. <br/>
The package and package-lock files contain the project's information for node.js, while the tsconfig file holds the setup for typescript compilation. The file webpack.config bundles the script files, but needs the public folder accessible. It also contains the information on redirecting calls to littlemaze/api/, so should one change the port number of the API server, it should also be adjusted here.
The public folder contains the main HTML file, images and the CSS-file. <br/>
The src folder holds the react-scripts responsible for the creation of the webpages. index.tsx is the main file connecting to the index.html and rendering the App. The App creates either the login page, the initialize game/play game page or the score/end page. The construction of these pages happens through the -Page.tsx files, which contain the functionality and the calls to the API server, and the corresponding components in the same folders. The typefiles-folder contains interfaces for typescript to use as object corresponding to the json information transmitted from the API server.

This server handles only HTTPS requests and does so with key pairs created and authorized by mkcert. This is an additional tool that may need to be installed first with choco (see also the makeSSLkeys.txt file). In case of a firefox browser on windows (yeah windows :p) the mkcert CA needs to be added to the trusted CA's through firefox settings (see also the makeSSLkeys.txt file). In case a self-signed pair suffices, these can be generated with openssl (installed on most systems and described also in the makeSSLkeys.txt file) and passed into the webpack server call in the `../runFEserver.bash` file.

## Output

- A server listening on port 2200 **Note that this is a HTTPS connection**
- A login page to get a user name and password
- A start page to get a player's name and a maze size
- The actual maze for the player to get lost in
- An overview of the scores if the game is won.

## Input

- POST and PUT requests to port 2222/littlemaze/api/...

## Usage

Just run the `../runFEserver.bash` file from the project's main folder. It will install the necessary node.js dependencies and start the server.
It will also create a new pair of keys for setting up the HTTPS connection using mkcert. The key files are called localhost.pem and localhost-key.pem corresponding to the localhost domain they were issued for.

To change the port number, adjust the port value in the webpack.config file.
