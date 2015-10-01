# iss-sys-int

Semestral work for subject System Integration with JBoss.

## Development

Build project

    mvn clean install

this created a jar in `docker` directory to be loaded into the docker image.

Build the docker image

    docker build -t iss/int docker
    
this created an image called `iss/int` from dockerfile in `docker` directory

Run the image

    docker run --name int -p 8081:8080 -p 8181:8181 iss/int

- Wilfly (apiman) at: http://docker-ip-address:8081 (http://docker-ip-address:8081/apimanui)
- JBoss Fuse console at: http://docker-ip-address:8181

## Linking with provided image (jpechane/course-sys-int-systems)

To run the both images (the provided systems and the integration), run the provided image as:

    docker run --name sys -d -p 9092:9092 -p 8080:8080 -p 8443:8443 jpechane/course-sys-int-systems

then run the integration image as:

    docker run --name int -d --link sys -p 8181:8181 -p 8081:8080 iss/int

Now the containers are linked together.

**For integrating the systems in the project always use the ENV variables to get the right IP address / port to the linked container.**
