FROM tomcat:11.0.27-jdk25-temurin-resolute

RUN rm -rf /usr/local/tomcat/webapps/*

COPY ./target/IT_M_JAVA_Exchange_Currency-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
