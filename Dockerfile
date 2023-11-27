FROM openjdk:17
COPY "./target/pijamasverito-1.jar" "app.jar"
EXPOSE 8086
ENTRYPOINT [ "java", "-jar", "app.jar" ]