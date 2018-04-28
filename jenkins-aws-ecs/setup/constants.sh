#!/usr/bin/env bash

# Common constants
export AWS_LOG_FILE=aws.log
export REGION=eu-central-1
export APP_NAME=extended-java-shop
export CLUSTER_NAME=${APP_NAME}-cluster
export AMI_ID=ami-9fc39c74 # This is for region eu-central-1 (Frankfurt)
export KEY_PAIR_NAME=${APP_NAME}-key-pair
export PRIVATE_KEY_FILE=${KEY_PAIR_NAME}_private.pem
export SECURITY_GROUP_NAME=${APP_NAME}-security-group
export NUMBER_OF_EC2_INSTANCES=5
export ECS_INSTANCE_ROLE=ecsInstanceRole # AWS needs this role to be called explicitly this, don't change it!
export EC2_FOR_ECS_POLICY_ARN=arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role # Like previous
#export ECS_SERVICE_ROLE=ecsServiceRole # Like previous
#export EC2_CONTAINER_SERVICE_ARN=arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceRole  # Like previous
export TAG_KEY=Group
export TAG_VALUE=${APP_NAME}

# Useful functions
run_aws() {
    cmd="aws $*"
    echo "Running '${cmd}'..." >>${AWS_LOG_FILE}
    result=`eval ${cmd}`
    echo ${result} >>${AWS_LOG_FILE}
    export AWS_LAST_RESULT=${result}
}

add_ingress_rule() {
    security_group_name=$1
    ports=$2

    run_aws ec2 authorize-security-group-ingress \
        --group-name ${security_group_name} \
        --protocol tcp --port ${ports} --cidr 0.0.0.0/0
}

create_iam_role() {
    role_name=$1
    policy_arn=$2
    role_description=$3

    echo "Creating IAM role '${role_name}' (${role_description})."

    # Create temporary file for role creation
    TEMP=".tmp.policy.document.json"
    cat >${TEMP} <<EOF
{
    "Version": "2008-10-17",
    "Statement": [
        {
            "Sid": "",
            "Effect": "Allow",
            "Principal": {
                "Service": "ec2.amazonaws.com"
            },
            "Action": "sts:AssumeRole"
        }
    ]
}
EOF

    run_aws iam create-role \
        --role-name ${role_name} \
        --description ${role_description} \
        --assume-role-policy-document file://${TEMP}

    rm -f ${TEMP}

    run_aws iam attach-role-policy \
        --policy-arn ${policy_arn} \
        --role-name ${role_name}
}

delete_iam_role() {
    role_name=$1
    policy_arn=$2

    echo "Deleting IAM role '${role_name}'..."

    run_aws iam detach-role-policy \
        --role-name ${role_name} \
        --policy-arn ${policy_arn}

    run_aws iam delete-role --role-name ${role_name}
}

count_non_terminated_instances() {
    run_aws ec2 describe-instances
    number_of_non_terminated_instances=`echo ${AWS_LAST_RESULT} | jq .Reservations[].Instances[].State.Name | grep -v terminated | wc -l`
}

