# iss-sys-int

Semestral work for subject System Integration with JBoss.

## Development

Build project

    mvn clean install

Run the project locally (without project's docker image; you still need the provided image with the systems to integrate)

    mvn camel:run

### Usage with Docker

#### Provided image
Run the provided image with the systems to integrate (you can do this once even though you start/stop/rebuild the integration project image).

    docker run --name sys -d -p 8082:8082 -p 9092:9092 -p 8080:8080 -p 8443:8443 jpechane/course-sys-int-systems

#### Project image

Add `jboss-fuse.zip` with JBoss Fuse installation to `docker/base` folder.

Build the base image (with Fuse, Wildfly, Apiman); needed only once unless you change something in the `docker/base` folder. On the UNIX system is nescessary `chmod +x docker/base/install.sh`

    docker build -t iss/int-base docker/base

Compile project (needed with every update in the project source to be reflected in the image)

    mvn clean install

.. this also copies the compiled JAR with the project into `docker/main` folder, so it can be loaded into docker (see the next command).

Build the project main image (needed with every update in the project source to be reflected in the image)

    docker build -t iss/int docker/main

Run the project's main image

    docker run --name int -it --link sys -p 8181:8181 -p 8081:8080 -p 8444:8444 iss/int
    
Now the containers (`sys` - theirs; and `int` - ours) are linked together. After the container boots up, the JBoss Fuse console should be ready to use. Also:

- Wilfly (apiman) at: `http://<docker-ip-address>:8081` (`http://<docker-ip-address>:8081/apimanui`)
- JBoss Fuse Web Console at: `http://<docker-ip-address>:8181`
- ... add additional port mappings (`-p <local-port>:<container-port>`) if needed to test something

Run these commands in the fuse console to manually load the project feature to Karaf (until hot-deploy in `deploy` folder works :-( )

    features:addurl file:/opt/jboss/jboss-fuse/deploy/sys-int-feature.xml
    features:install sys-int

The featured bundle should start the project with camel routes..

**For integrating the systems in the project, always use the ENV variables to get the right IP address and port to the linked container (see `src/main/resources/application.properties` for examples).** Docker when linking images automatically creates these env variables so we can easily refer to the other running container.

### Iteration

With every change in the project source reinstall, rebuild and deploy:

    mvn clean install
    
    docker stop int; docker rm int              (stop and remove the container if it was running)
    docker build -t iss/int docker/main
    docker run --name int -it --link sys -p 8181:8181 -p 8081:8080 -p 8444:8444 iss/int
    
    # once Fuse console loaded
    features:addurl file:/opt/jboss/jboss-fuse/deploy/sys-int-feature.xml
    features:install sys-int

If you change something in the image configuration only (`docker/main`), you don't have to run `maven clean install`.

Or better run the project locally first before running it in docker.
