# Little Maze Database

Here the creation of the MySQL-server in a docker container is managed.

There are two versions, one for the test-database, used by the API-ConnectionTester, and the main database for the game. The Dockerfiles handle the set-up for the docker image, with the SQL-DB initiation and user-permissions in the corresponding subfolders preloaded. The ../runDocker(Test)DB.bash files start the correct database, that then listens on port 4444 (port 3306 is the docker-network port and can't be directly accessed from "outside").

**At the moment, data entered into the DB is not conserved on the ending of the docker container.** Also, if restarting the docker container, make sure to `docker kill` any running containers and `docker rm` them as well. To see existing containers, use `docker container ls`.
