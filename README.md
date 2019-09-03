# k8s-spring-boot-helloworld-liveness-readiness-probes

Taking the Basic Hello World Application in Spring Boot! further. This example creates a docker container containing a Spring Boot application with a Controller that returns "Hello World!" which gets deployed to a kubernetes luster using helm.


## Prerequisites

- Java IDE (I am using IntelliJ CE)
- Maven
- Docker
- Helm


## System Configuration at time of test

- macOS Mojave - Version 10.14.6
- IntelliJ CE - Version CE 2019.2
- Maven - Version 3.6.1
- Docker Desktop - Version 2.1.0.1 (37199)
- Kubernetes - 
- Helm - Version 

## Initial Setup

### Creating Spring project

Follow the steps outlined in [docker-spring-boot-helloworld](https://github.com/ameyrupji/docker-spring-boot-helloworld) GitHub project to create a Spring Boot application.

```
mvn clean install
docker build -t spring-boot-helloworld:v1 .
```

`helm lint ./helm`

`helm install --name helloworld ./helm`

`helm ls`

`helm upgrade helloworld ./helm`


## Test 


Run the following command to ensure the server is running: `curl http://localhost:31000/helloworld/`

You can also view it in the browser by going to `http://localhost:31000/helloworld/` and following response will show up:


## Cleanup

`helm delete --purge helloworld`


To stop the container that is running use this command: `docker stop {container_id}`

To delete the container that was created use this command: `docker rm {container_id}`

To delete the docker image that was created: `docker rmi {image_id}`

## Useful links

- https://www.baeldung.com/spring-boot-kubernetes-self-healing-apps
- https://www.baeldung.com/kubernetes-helm
- https://medium.com/@pablorsk/kubernetes-helm-node-hello-world-c97d20437abd
- https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html
