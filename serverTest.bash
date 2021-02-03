# Run in seperate terminal from serverRun, optionally puts whole body in text file
echo "Testing GET request"
curl "http://localhost:2222/littlemaze" #-o "GET_output.html"
echo

echo "Testing POST request"
curl -X POST -H "Content-Type: application/json" \
 -d '{"nameplayer":"Silke","gridSize":"2"}' \
 "http://localhost:2222/littlemaze/api/start" #-o "POST_output.html"
