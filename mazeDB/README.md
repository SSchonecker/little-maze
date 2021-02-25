# Little Maze Database

Here the creation of the MySQL-server in a docker container is managed.

There are two versions, one for the test-database, used by the API-ConnectionTester, and the main database for the game. The Dockerfiles handle the set-up for the docker image, with the SQL-DB initiation and user-permissions in the corresponding subfolders preloaded. The ../runDocker(Test)DB.bash files start the correct database, that then listens on port 4444 (port 3306 is the docker-network port and can't be directly accessed from "outside").

Data entered into the main database is conserved with a docker volume. Ending the ../runDocker(Test)DB.bash stops or kills the docker container. It still exists and can be restarted with `docker start`. However, removing the container with `docker rm` completely removes the container **and the connected volume with the data** as well. To see existing containers, use `docker container ls`.
