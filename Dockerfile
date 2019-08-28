FROM openjdk:8-jre-alpine
COPY ./target/com.ameyrupji.helloworld-1.0-SNAPSHOT.jar /helloworld.jar

CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=default", "/helloworld.jar"]

#ALTERNATE IMPLEMENTATION (VALIDATE!!!!)
# exec java -Dspring.profiles.active=default -jar /helloworld.jar