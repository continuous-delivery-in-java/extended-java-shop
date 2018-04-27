# Jenkins - AWS ECS 
A sample Jenkins server with pre-created jobs that will deploy the three sample services to an Amazon Cloud (ECS)

## Pre-requisites
- An account for AWS ECS is necessary.
- Install AWS - CLI
- Set up AWS CLI locally
- Run setup/create-env.sh

## Procedure
1. Run `build.sh` to generate the necessary config files and docker image
1. Run `docker-compose -f docker-compose.yaml up` to run the generated service; the Jenkins server is available at `http://localhost:8080/`
1. SSH into Jenkins to configure AWS credentials (get the key first from console, etc)
1. After running the corresponding deployment jobs in Jenkins, run `expose-services.sh` to obtain the URLs where each service is available. 
