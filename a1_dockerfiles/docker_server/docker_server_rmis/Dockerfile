FROM ubuntu
RUN apt-get update
RUN apt-get install -y default-jre
RUN apt-get install -y net-tools
COPY GenericNode.jar /
COPY runserver.sh /
ENTRYPOINT ["/runserver.sh"]

