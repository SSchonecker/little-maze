cd mazeDB/
docker build -t dmysql -f Dockerfile .
echo "-------------------- Succesfully build image here ---------------------"
docker run -p 4444:3306 --name dmysql_cont dmysql
docker kill dmysql_cont
