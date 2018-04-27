#!/usr/bin/env bash

export AWS_LOG_FILE=aws.log
export APP_NAME=extended-java-shop
export CLUSTER_NAME=${APP_NAME}-cluster
export AMI_ID=ami-9fc39c74 # This is for region eu-central-1 (Frankfurt)
export KEY_PAIR_NAME=${APP_NAME}-key-pair
export PRIVATE_KEY_FILE=${KEY_PAIR_NAME}_private.pem
export SECURITY_GROUP_NAME=${APP_NAME}-security-group
export NUMBER_OF_EC2_INSTANCES=5
export ECS_INSTANCE_ROLE=ecsInstanceRole # AWS needs this role to be called explicitly this, don't change it!
export EC2_FOR_ECS_POLICY_ARN=arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role # Like previous

run_aws() {
    cmd="aws $*"
    echo "Running '${cmd}'..." >>${AWS_LOG_FILE}
    result=`eval ${cmd}`
    echo ${result} >>${AWS_LOG_FILE}
    export AWS_LAST_RESULT=${result}
}
