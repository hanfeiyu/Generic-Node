FROM ubuntu
RUN apt-get update
RUN apt-get install -y default-jre 
RUN apt-get install -y net-tools
COPY GenericNode.jar /
COPY entrypoint_client.sh /
COPY bigtest_tc.sh /
COPY bigtest_uc.sh /
COPY bigtest_rc.sh /
COPY bigtest_tc_concurrency.sh /
COPY bigtest_uc_concurrency.sh /
COPY bigtest_rc_concurrency.sh /
ENTRYPOINT ["/entrypoint_client.sh"]
CMD ["300"]


