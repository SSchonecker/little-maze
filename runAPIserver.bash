cd mazeDomain
mvn clean install

cd ../mazeAPI
mvn clean package
mvn jetty:run