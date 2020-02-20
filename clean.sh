#! /bin/bash


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


