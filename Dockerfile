FROM tomcat:11.0.27-jdk25-temurin-resolute

COPY currencyExchange8080.war /usr/local/tomcat/webapps/currencyExchange.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
