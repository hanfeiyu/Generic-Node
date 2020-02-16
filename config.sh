#! /bin/bash

DOCKER_CLIENT_PATH="./a1_dockerfiles/docker_client"

DOCKER_SERVER_PATH="./a1_dockerfiles/docker_server"

JAR_TARGET_PATH="./tcss558_assignment_1/target/GenericNode-0.0.1-SNAPSHOT.jar"

JAR_FILE="GenericNode.jar"


# Maven clean and install 
echo -e "\n********** Maven clean and install **********\n"

cd ./tcss558_assignment_1/
mvn clean install
cd ../

echo -e "\n********** Maven done **********\n"


# Clean and enable docker test
echo -e "\n********** Generating jar file... **********\n"

if [ -f "$DOCKER_CLIENT_PATH/$JAR_FILE" ]
then
    rm $DOCKER_CLIENT_PATH/$JAR_FILE
    cp $JAR_TARGET_PATH $DOCKER_CLIENT_PATH/$JAR_FILE
else
    cp $JAR_TARGET_PATH $DOCKER_CLIENT_PATH/$JAR_FILE
fi

if [ -f "$DOCKER_SERVER_PATH/docker_server_ts/$JAR_FILE" ]
then
    rm $DOCKER_SERVER_PATH/docker_server_ts/$JAR_FILE
    cp $JAR_TARGET_PATH $DOCKER_SERVER_PATH/docker_server_ts/$JAR_FILE
else
    cp $JAR_TARGET_PATH $DOCKER_SERVER_PATH/docker_server_ts/$JAR_FILE
fi

if [ -f "$DOCKER_SERVER_PATH/docker_server_us/$JAR_FILE" ]
then
    rm $DOCKER_SERVER_PATH/docker_server_us/$JAR_FILE
    cp $JAR_TARGET_PATH $DOCKER_SERVER_PATH/docker_server_us/$JAR_FILE
else
    cp $JAR_TARGET_PATH $DOCKER_SERVER_PATH/docker_server_us/$JAR_FILE
fi

if [ -f "$DOCKER_SERVER_PATH/docker_server_rmis/$JAR_FILE" ]
then
    rm $DOCKER_SERVER_PATH/docker_server_rmis/$JAR_FILE
    cp $JAR_TARGET_PATH $DOCKER_SERVER_PATH/docker_server_rmis/$JAR_FILE
else
    cp $JAR_TARGET_PATH $DOCKER_SERVER_PATH/docker_server_rmis/$JAR_FILE
fi

echo -e "\n********** jar file done **********\n"


# Stop old docker containers
echo -e "\n********** Stopping old docker containers **********\n"

docker_pid=`docker ps -a | grep -E '.*((tcss558server+)|(tcss558client+))+.*' | awk '{print $1}'`

if [ ! -n "$docker_pid" ]
then
    echo "No related container is running"
else
    for pid in $docker_pid
    do
        sudo docker stop $pid
        echo "Container $pid stopped"
    done
fi

echo -e "\n********** Stop containers done **********\n"

# Remove old docker images
echo -e "\n********** Removing old docker images **********\n"

docker_image_id=`docker images | grep -E '.*((tcss558server+)|(tcss558client+))+.*' | awk '{print $3}'`

if [ ! -n "$docker_image_id" ] 
then
    echo "No related image created"
else
    for image in $docker_image_id
    do 
        sudo docker rmi $image
        echo "Image $image removed"
    done
fi

echo -e "\n********** Remove images done **********\n"


# Build docker_server and docker_client
echo -e "\n********** Build new images and containers for server/client **********\n"

cd $DOCKER_CLIENT_PATH
sudo docker build -t tcss558client .
echo -e "tcss558client image created\n"
cd ../../

cd $DOCKER_SERVER_PATH/docker_server_ts
sudo docker build -t tcss558server_ts .
echo -e "tcss558server_ts image created\n"
cd ../../../

cd $DOCKER_SERVER_PATH/docker_server_us
sudo docker build -t tcss558server_us .
echo -e "tcss558server_us image created\n"
cd ../../../

cd $DOCKER_SERVER_PATH/docker_server_rmis
sudo docker build -t tcss558server_rmis .
echo -e "tcss558server_rmis image created"
cd ../../../

echo -e "\n********** Ready to run **********\n"

sudo docker images


# Run each container
echo -e "\n********** Run containers **********\n"

sudo docker run -d --rm tcss558client
echo -e "tcss558client is running\n"

sudo docker run -d --rm tcss558server_ts
echo -e "tcss558server_ts is running\n"

sudo docker run -d --rm tcss558server_us
echo -e "tcss558server_us is running\n"

sudo docker run -d --rm tcss558server_rmis
echo -e "tcss558server_rmis is running"

echo -e "\n********** Containers ready to exexcute **********\n"

sudo docker ps -a
