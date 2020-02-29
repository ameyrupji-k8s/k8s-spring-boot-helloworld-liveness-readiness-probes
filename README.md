| [◂ Previous](https://github.com/ameyrupji-k8s/k8s-spring-boot-helloworld) |
|-----|

# k8s-spring-boot-helloworld-liveness-readiness-probes

Taking the Kubernetes Spring Boot Hello World Application further. This example adds readiness and liveness probes. Readiness probes helps to know when a Container is ready to start accepting traffic. Liveness probes helps to know when to restart a Container. For example, liveness probes could catch a deadlock, where an application is running, but unable to make progress.

Read more about liveness and readiness probes [here](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/).


## Prerequisites

- Java IDE (I am using IntelliJ CE)
- Maven
- Docker
- Helm
- Tiller on the Kubernetes cluster

## System Configuration at time of test

- macOS Catalina - Version 10.15.3 (19D76)
- IntelliJ CE - Version CE 2019.3
- Maven - Version 3.6.1
- Docker Desktop - Version 2.2.0.3 (42716)
- Kubernetes - v1.15.5
- Helm - v2.14.3

## Initial Setup

### Adding Spring Boot Actuator to the Application

Add this code to this `pom.xml` Maven build file adding dependency of Spring Boot Actuator if already not added to your project.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
    <version>2.2.5.RELEASE</version>
</dependency>
```

Also need to enable endpoints actuator endpoints by adding the following lines to `/src/main/resources/application.properties`

```properties
# Spring Boot Actuator
management.endpoints.enabled-by-default=true
```

**Note:** Get the latest release from [here](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator)

More information about Spring Boot Actuator can be found [here](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-health).

### Adding liveness probe

Add this code to this `spring-boot-helloworld-chart/templates/deployment.yaml` in the `spec.template.spec.containers[0]` section
```yaml
          livenessProbe:
            httpGet:
              path: /actuator/health
              port: {{ .Values.deployment.port }}
            initialDelaySeconds: 30
            periodSeconds: 30
            failureThreshold: 3
```

This will check to see an HTTP 200 response from the endpoint `/actuator/health` at the deployment port every 30 seconds (`periodSeconds`) after an initial delay (`initialDelaySeconds`) of 30 seconds for a maximum of 3 times (`failureThreshold`) after which it is going to restart the container for which this liveness probe is added which is the container running the spring boot hello world app.

### Adding readiness probe

Add this code to this `spring-boot-helloworld-chart/templates/deployment.yaml` in the `spec.template.spec.containers[0]` section
```yaml
          readinessProbe:
            httpGet:
              path: /actuator/health
              port: {{ .Values.deployment.port }}
            initialDelaySeconds: 15
            periodSeconds: 30
            failureThreshold: 3
```

This will check to see an HTTP 200 response from the endpoint `/actuator/health` at the deployment port every 30 seconds (`periodSeconds`) after an initial delay (`initialDelaySeconds`) of 15 seconds for a maximum of 3 times (`failureThreshold`) after which it is going to Pod will be marked container as `Unready` and no traffic will be sent to it for which this readiness probe is added which is the container running the spring boot hello world app.

### Adding ability to deploy same pod again

Add this code to this `spring-boot-helloworld-chart/templates/deployment.yaml` in the `spec.template.metadata` section
```yaml
      annotations:
        timestamp: "{{ date "Mon Jan _2 15:04:05 2006" .Release.Time }}"
```

The `"Mon Jan _2 15:04:05 2006"` is the format of the date and the `.Release.Time` will use the time of the deployment.

To read more about date formats click [here](https://golang.org/src/time/format.go). To read more about annotations click [here](https://kubernetes.io/docs/concepts/overview/working-with-objects/annotations/).

### Adding Deployment Strategy

Add this code to this `spring-boot-helloworld-chart/templates/deployment.yaml` in the `spec` section
```yaml
  strategy:
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
```

This tell kubernetes how to replace old Pods by new ones. In this case I am using the RollingUpdate (`rollingUpdate`). The `maxSurge` of 1 specifies maximum number of Pods that can be created over the desired number of Pods `1` in this case. The `maxUnavailable` specifies the maximum number of Pods that can be unavailable during the update process `0` in this case.

**Note:** Rebuild your docker container using `docker build -t spring-boot-helloworld:v1 .` as there is a slight change also to the `Dockerfile` which now calls a `procfile` on container initialization. We will use this in the future.


## Test 

Install container using helm command

`helm install --name spring-boot-helloworld -f ./spring-boot-helloworld-chart/values.yaml ./spring-boot-helloworld-chart/`

Alternatively you can also use `upgrade` command with the `--install` flag:

`helm upgrade spring-boot-helloworld ./spring-boot-helloworld-chart/ -f ./spring-boot-helloworld-chart/values.yaml --install --namespace default`

You can see that the pos is in the initializing state through the terminal and the dashboard:

![terminal-upgrade-command](images/terminal-upgrade-command.png)

![terminal-list-pods-initializing](images/terminal-list-pods-initializing.png)

![safari-dashboard-pods-initializing](images/safari-dashboard-pods-initializing.png)


You can see that the pod is started in about 30 seconds and  by going to `http://localhost:31000/helloworld/` and following response will show up. The actuator endpoint will also show status as `UP`:

![safari-localhost-helloworld](images/safari-localhost-helloworld.png)

![safari-localhost-actuator-health-up](images/safari-localhost-actuator-health-up.png)


Now Update App using helm command

`helm upgrade spring-boot-helloworld ./spring-boot-helloworld-chart/ -f ./spring-boot-helloworld-chart/values.yaml --install --namespace default`

View the new pod getting created through the dashboard and the old pod getting cycled out.

![terminal-helm-update-old-present](images/terminal-helm-update-old-present.png)

![safari-dashboard-pods-initializing-old-present](images/safari-dashboard-pods-initializing-old-present.png)

Old pod will be deleted eventually to see only one pod on dashboard:

![safari-dashboard-update-complete](images/safari-dashboard-update-complete.png)

View the app again in a browser `http://localhost:31000/helloworld/`:

![safari localhost](images/safari-localhost-helloworld.png)


## Cleanup

To stop the container that is running use this command: `helm delete --purge spring-boot-helloworld`

![terminal helm delete purge](images/terminal-helm-delete-purge.png)


## Useful links

- https://www.baeldung.com/spring-boot-kubernetes-self-healing-apps
- https://www.baeldung.com/kubernetes-helm
- https://medium.com/@pablorsk/kubernetes-helm-node-hello-world-c97d20437abd
- https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html
- https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/
- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator
- https://golang.org/src/time/format.go
- https://kubernetes.io/docs/concepts/overview/working-with-objects/annotations/
- https://medium.com/@chunjenchen/helm-upgrade-is-not-recreating-pod-f1813ce8e55a

| [Next ▸](https://github.com/ameyrupji-k8s/k8s-spring-boot-helloworld-security-context) |
|-----|
