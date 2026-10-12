# Используем официальный образ Tomcat на базе Eclipse Temurin (Java 17)
# Вы можете изменить тег (например, 10.1-jdk17 или 9.0-jdk11) под требования вашего приложения
FROM tomcat:10.1-jdk17-temurin-jammy

# Удаляем стандартные предустановленные приложения Tomcat (по желанию, для безопасности)
RUN rm -rf /usr/local/tomcat/webapps/*

COPY IT_M_JAVA_Exchange_Currency-1.0.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
