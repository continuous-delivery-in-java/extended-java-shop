#!/bin/bash

echo "Deploying service '${project_name}'"

cp jenkins-aws-ecs/task-definitions/${project_name}-task.json taskdef.json

# The below has been dapted from https://docs.aws.amazon.com/AWSGettingStartedContinuousDeliveryPipeline/latest/GettingStarted/CICD_Jenkins_Pipeline.html

#Load Constants
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

pushd ${SCRIPT_DIR}
source ./setup/constants.sh
popd

FAMILY=`sed -n 's/.*"family": "\(.*\)",/\1/p' taskdef.json`
SERVICE_NAME=${project_name}-service

#Replace the build number and respository URI placeholders with the constants above
cp taskdef.json ${project_name}-v_${BUILD_NUMBER}.json
#Register the task definition in the repository
aws ecs register-task-definition --family ${FAMILY} --cli-input-json file://${WORKSPACE}/${project_name}-v_${BUILD_NUMBER}.json --region ${REGION}
SERVICES=`aws ecs describe-services --services ${SERVICE_NAME} --cluster ${CLUSTER_NAME} --region ${REGION} | jq .failures[]`
#Get latest revision
REVISION=`aws ecs describe-task-definition --task-definition ${project_name} --region ${REGION} | jq .taskDefinition.revision`

#Create or update service
if [ "$SERVICES" == "" ]; then
    echo "entered existing service"
    DESIRED_COUNT=`aws ecs describe-services --services ${SERVICE_NAME} --cluster ${CLUSTER_NAME} --region ${REGION} | jq .services[].desiredCount`
    if [ "${DESIRED_COUNT}" == "0" ]; then
        DESIRED_COUNT="1"
    fi
    aws ecs update-service --cluster ${CLUSTER_NAME} --region ${REGION} --service ${SERVICE_NAME} --task-definition ${FAMILY}:${REVISION} --desired-count ${DESIRED_COUNT}
else
    echo "entered new service"
    get_current_namespace # namespace's ARN is available in variable ${namespace_arn} after this call
    aws ecs create-service --service-name ${SERVICE_NAME} --desired-count 1 --task-definition ${FAMILY} --cluster ${CLUSTER_NAME} --region ${REGION} --service-registries registryArn=${namespace_arn}
fi

