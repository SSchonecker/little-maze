cd mazeDB/
docker build -t dmysql -f Dockerfile .
echo "-------------------- Succesfully build image here ---------------------"
#docker run -p 4444:3306 --name dmysql_cont dmysql
#docker kill dmysql_cont
#docker rm dmysql_cont

if [ ! "$(docker ps -a -f name=dmysql_cont | wc -l ) -eq 2" ]; then
    echo "-------------------- Building new container --------------------"
    docker run -p 4444:3306 --name dmysql_cont dmysql
else
	echo "-------------------- Starting old container --------------------"
	docker container start dmysql_cont
fi
docker kill dmysql_cont
