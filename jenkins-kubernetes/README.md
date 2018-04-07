# Jenkins - Kubernetes
A sample Jenkins server with pre-created jobs that will deploy the three sample services to a kubernetes cluster.

## Pre-requisites
`minikube` must be installed locally and running; this will create a minimal kubernetes cluster that Jenkins jobs will deploy to.

## Procedure
1. Run `build.sh` to generate the necessary config files and docker image
1. Run `docker-compose -f docker-compose.yaml up` to run the generated service
1. After running the corresponding deployment jobs, run `expose-services.sh` to obtain the URLs where each service is available. 
