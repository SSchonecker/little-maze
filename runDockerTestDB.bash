cd mazeDB/
docker build -t dmysqltest -f test.Dockerfile .
echo "-------------------- Succesfully build image here ---------------------"
docker run -p 4444:3306 dmysqltest
