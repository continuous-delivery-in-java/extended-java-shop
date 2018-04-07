# Jenkins - Kubernetes
A sample Jenkins server with pre-created jobs that will deploy the three services to a kubernetes cluster.

## Pre-requisites
`minikube` must be installed locally and running

## Procedure
1. Run `build.sh` to generate the necessary config files and docker image
1. Run `docker-compose -f docker-compose.yaml` to run the generated service
1. After running the corresponding deployment jobs, run `expose-services.sh` to obtain the URLs where each service is available. 