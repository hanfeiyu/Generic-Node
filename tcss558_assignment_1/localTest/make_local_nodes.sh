#! /bin/bash

# Maven clean and install 
cd ..
mvn clean install
cd localTest

# Enable local test
cp ../target/genericNode-0.0.1-SNAPSHOT.jar genericNodeServer.jar
cp ../target/genericNode-0.0.1-SNAPSHOT.jar genericNodeClient.jar

