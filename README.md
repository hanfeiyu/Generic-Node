# Devlopment Environment
>## Language
>Java

>## Language version
>java 11.0.6 2020-01-14 LTS

>## IDE
>Eclipse 2019-12

>## Package tool
>Maven 4.0.0

>## Development OS 
>Mac

>## Testing OS 
>Ubuntu 18.04 on VirtualBox

# Architecture
>.
>├── README.md
>├── a1_dockerfiles
>│   ├── docker_client
>│   └── docker_server
>├── config.sh
>├── tcss558_assignment_1
>│   ├── bin
>│   ├── localTest
>│   ├── pom.xml
>│   ├── src
>│   └── target
>└── tcss558_assignment_1_performance.pdf

# Usage
>Simply run `./config.sh` to start testing
>
## For Linux OS
>After the configuration finished, you should have 1 terminal(tab) for client
>and 3 terminals(tab) for ts/us/rmis servers
>
>In each server terminal:
>Use `ifconfig` to obtain IP address
>
>In the client terminal:
>Use `./bigtest_tc.sh <TCP IP address>" to run big test of TCP
>Use `./bigtest_uc.sh <UDP IP address>" to run big test of UDP
>Use `./bigtest_rc.sh <RMI IP address>" to run big test of RMI
>
## For other OSs
>After the configuration finished, you should have 4 containers runing for
>client and ts/us/rmis servers
>
>Open each with `sudo docker exec -it <Container ID> bash`
>
>In each server terminal:
>Use `ifconfig` to obtain IP address
>
>In the client terminal:
>Use `./bigtest_tc.sh <TCP IP address>" to run big test of TCP
>Use `./bigtest_uc.sh <UDP IP address>" to run big test of UDP
>Use `./bigtest_rc.sh <RMI IP address>" to run big test of RMI
>


