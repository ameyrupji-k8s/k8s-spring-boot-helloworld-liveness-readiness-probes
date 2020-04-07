FROM openjdk:8-jre-alpine
COPY ./target/com.ameyrupji.helloworld-1.0-SNAPSHOT.jar /helloworld.jar

# OPTION 1: Inline -
#CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=default", "/helloworld.jar"]

# OPTION 2: procfile -
COPY ./procfile procfile
RUN chmod 755 /procfile

# Removing exec will break graceful shutdown hooks.
CMD exec /procfile
