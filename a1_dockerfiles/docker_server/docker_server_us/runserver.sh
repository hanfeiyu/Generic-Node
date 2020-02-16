#!/bin/bash
# Dummy jar file
#java -jar GenericNode.jar

#TCP Server
#java -jar GenericNode.jar ts 1234

#UDP Server
java -jar GenericNode.jar us 1235

#RMI Server
#rmiregistry -J-Djava.class.path=GenericNode.jar &
#java -Djava.rmi.server.codebase=file:GenericNode.jar -cp GenericNode.jar genericnode.GenericNode rmis 
#java -jar GenericNode.jar rmis 1234
