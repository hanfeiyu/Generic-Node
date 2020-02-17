#! /bin/bash


DOCKER_CLIENT_PATH="./a1_dockerfiles/docker_client"

DOCKER_SERVER_PATH="./a1_dockerfiles/docker_server"

DOCKER_SERVER_TYPE=("tcss558server_ts" "tcss558server_us"
    "tcss558server_rmis")

JAR_TARGET_PATH="./tcss558_assignment_1/target/GenericNode-0.0.1-SNAPSHOT.jar"

JAR_FILE="GenericNode.jar"


#
# Maven clean and install 
#
echo -e "\n********** Maven clean and install **********\n"

cd ./tcss558_assignment_1/
mvn clean install
cd ../

#
# Clean and enable docker test
#
echo -e "\n********** Generating jar file... **********\n"

# Client
if [ -f "$DOCKER_CLIENT_PATH/$JAR_FILE" ]
then
    rm $DOCKER_CLIENT_PATH/$JAR_FILE
    cp $JAR_TARGET_PATH $DOCKER_CLIENT_PATH/$JAR_FILE
else
    cp $JAR_TARGET_PATH $DOCKER_CLIENT_PATH/$JAR_FILE
fi

echo "$DOCKER_CLIENT_PATH/$JAR_FILE created"

# Server
for server in ${DOCKER_SERVER_TYPE[*]}
do
    if [ -f "$DOCKER_SERVER_PATH/$server/$JAR_FILE" ]
    then
        rm $DOCKER_SERVER_PATH/$server/$JAR_FILE
        cp $JAR_TARGET_PATH $DOCKER_SERVER_PATH/$server/$JAR_FILE
    else
        cp $JAR_TARGET_PATH $DOCKER_SERVER_PATH/$server/$JAR_FILE
    fi

    echo "$DOCKER_SERVER_PATH/$server/$JAR_FILE created"
done

#
# Stop old docker containers
#
echo -e "\n********** Stopping old docker containers **********\n"

docker_pid=`sudo docker ps -a | grep -E '.*((tcss558server+)|(tcss558client+))+.*' | awk '{print $1}'`

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

#
# Remove old docker images
#
echo -e "\n********** Removing old docker images **********\n"

docker_image_id=`sudo docker images | grep -E '.*((tcss558server+)|(tcss558client+))+.*' | awk '{print $3}'`

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

#
# Build docker_server and docker_client
#
echo -e "\n********** Build new images and containers for server/client **********\n"

# Client
cd $DOCKER_CLIENT_PATH
sudo docker build -t tcss558client .
echo "tcss558client image created"
cd ../../

# Server
for server in ${DOCKER_SERVER_TYPE[*]}
do
    cd $DOCKER_SERVER_PATH/$server
    sudo docker build -t $server .
    echo "$server image created"
    cd ../../../
done

# Clear dangling images if any exists
docker_dangling_images=`sudo docker images | grep "none" | awk '{print $3}'`

if [ -n "$docker_dangling_images" ]
then
    echo "Remove dangling images"
    echo $docker_dangling_images | xargs sudo docker rmi
fi

echo -e "\n"
sudo docker images

#
# Run each container
#
echo -e "\n********** Run containers **********\n"

# Client
sudo docker run -d --rm tcss558client
echo "tcss558client is running"

# Server
for server in ${DOCKER_SERVER_TYPE[*]}
do 
    sudo docker run -d --rm $server
    echo "$server is running"
done

echo -e "\n"
sudo docker ps -a

#
# Linux can go further while other OSs will stop here
#
if [ "$(uname)" != "Linux" ]
then
    echo -e "\nOS type: $(uname), doesn't support gnome-terminal\n"
    echo -e "\n********** Ready to test **********\n"
    exit
fi

#
# Install gnome-terminal if not installed
#
if [ -z "$(apt list --installed | grep gnome-terminal)" ]
then
    sudo apt install gnome-terminal
    echo "Install gnome-terminal"
fi

#
# Execute 3 servers and 1 client in total 4 containers, using terminals
# individually
#
echo -e "\n********** Open terminals for client and servers **********\n"
servers_info=`sudo docker ps -a | grep -E 'tcss558server+' | awk '{print $1" "$2}'`
client_pid=`sudo docker ps -a | grep -E 'tcss558client'  | awk '{print $1}'`

# Client
echo "Open client: tcss558client"
gnome-terminal --tab \
    -t "tcss558client" \
    -- bash -c "sudo docker exec -it $client_pid bash"

# Server
info_length=`echo $servers_info | wc -w`
for i in $(seq 1 2 $info_length)
do
    server_pid=`echo $servers_info | awk '{print \$"'$i'"}'`
    title=`echo $servers_info | awk '{print \$"'$(($i+1))'"}'`

    echo "Open server: $title"
    gnome-terminal --tab \
        -t "$title" \
        -- bash -c "sudo docker exec -it $server_pid bash"
done

echo -e "\n********** Ready to test **********\n"

