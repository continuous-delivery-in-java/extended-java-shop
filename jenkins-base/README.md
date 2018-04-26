# Jenkins - Base
A sample Jenkins server with pre-created jobs that builds everything, but doesn't deploy (there is a placeholder deploy job that doesn't do anything).

The placeholder deploy job will be overridden by other Jenkins docker containers that do implement different deployment mechanisms.

## Procedure
1. Run `build.sh` to generate the necessary config files and docker image
1. Run `docker-compose -f docker-compose.yaml up` to run the generated service; the Jenkins server is available at `http://localhost:8080/`
1. Once Jenkins is up and running, you will need to create a Credentials key with id "DockerHub" and your user and password for Docker Hub.
