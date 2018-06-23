# Jenkins - AWS ECS 
A sample Jenkins server with pre-created jobs that will deploy the three sample services to an Amazon Cloud (ECS)

## Pre-requisites
- jq needs to be installed
- An account for AWS ECS is necessary.
- Install AWS - CLI
- Set up AWS CLI locally: https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html
- Run setup/create-env.sh

## Procedure
1. Run `build.sh` to generate the necessary config files and docker image
1. Run `docker-compose -f docker-compose.yaml up` to run the generated service; the Jenkins server is available at `http://localhost:8080/`
1. Once the Jenkins image has started, you'll need to ssh into it and open up permissions of the Docker socket file (**warning:** there is a security risk on doing this, see below for details).
    1. Identify the Docker ID of the image: ``docker_id=`docker ps | grep jenkins-kubernetes | cut -f1 -d\  ` ``
    1. SSH into the Docker container: `docker exec -ti ${docker_id} bash` 
    1. Once inside, open up permissions for the Docker socket file: `sudo chmod 777 /run/docker.sock` 
1. SSH into Jenkins to configure AWS credentials (get the key first from console, etc)
1. Once Jenkins is up and running, you will need to create a Credentials key with id "DockerHub" and your user and password for Docker Hub.
1. After running the corresponding deployment jobs in Jenkins, run `expose-services.sh` to obtain the URLs where each service is available. 

### Security exposure of opening up permissions in Docker socket file
This CI/CD setup implies a Jenkins instance running inside a Docker container, with builds that invoke the `docker`
command themselves to pack and deploy applications as Docker images. This means that we are trying to run `docker`
within a Docker container. This is not trivial, and the implications are still being worked out. There are essentially two ways of achieving this:
- Running Docker-in-Docker, that is, running a second Docker daemon within the Docker container itself.
- Exposing the Docker daemon _in the host computer_ to the Docker containers, so running the command `docker` within the containers will communicate to the Docker daemon
_in the host computer._

Here we have opted for the latter, which is essentially achieved by making the Docker socket file accessible by any process... which isn't the safest of set-ups,
so please don't do this in your production environment. It is ok to do it (temporarily) in your local machine to try out the examples laid out here, but once you're
finished make sure to restore the permissions of the Docker socket file to its original settings; restarting the Docker server should do the trick.

More on Docker-in-Docker on this [excellent post by Jérôme Petazzoni](https://jpetazzo.github.io/2015/09/03/do-not-use-docker-in-docker-for-ci/). 
