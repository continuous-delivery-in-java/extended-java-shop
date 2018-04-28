#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

pushd ${SCRIPT_DIR}
source ./constants.sh

# delete EC2 instances
echo "Getting current instances..."
run_aws ec2 describe-instances --filter "'Name=tag:${TAG_KEY},Values=${TAG_VALUE}'"
instances=`echo ${AWS_LAST_RESULT} | jq .Reservations[].Instances[].InstanceId`

echo "Deleting current instances..."
run_aws ec2 terminate-instances --instance-ids ${instances}

count_non_terminated_instances
while [ ${number_of_non_terminated_instances} -ne 0 ]; do
    echo "Waiting for ${number_of_non_terminated_instances} instance(s) to be completely terminated..."
    count_non_terminated_instances
    sleep 5
done

# Delete private namespace
echo "Deleting DNS Service Discovery '${DNS_NAMESPACE}'"
get_current_namespace # Namespace ID is now available in variable ${namespace_id}
run_aws servicediscovery delete-namespace --id ${namespace_id}

# Delete IAM roles
# Removing role from instance profile first
run_aws iam remove-role-from-instance-profile \
    --instance-profile-name ${ECS_INSTANCE_ROLE} \
    --role-name ${ECS_INSTANCE_ROLE}

delete_iam_role ${ECS_INSTANCE_ROLE} ${EC2_FOR_ECS_POLICY_ARN}
#delete_iam_role ${ECS_SERVICE_ROLE} ${EC2_CONTAINER_SERVICE_ARN}

echo "Removing security group ${SECURITY_GROUP_NAME}"
run_aws ec2 delete-security-group --group-name ${SECURITY_GROUP_NAME}

echo "Removing key ${KEY_PAIR_NAME} associated private key ${PRIVATE_KEY_FILE}"
[ -f ${PRIVATE_KEY_FILE} ] && rm -f ${PRIVATE_KEY_FILE}
run_aws ec2 delete-key-pair --key-name ${KEY_PAIR_NAME}

echo "Removing services from cluster"
# Get services names
run_aws ecs list-services --cluster ${CLUSTER_NAME}
services=`echo ${AWS_LAST_RESULT} | jq .serviceArns[] | cut -d\" -f2 | cut -d\/ -f2`
for service in ${services}; do
    run_aws ecs delete-service --cluster ${CLUSTER_NAME} --region ${REGION} --service ${service}
done

echo "De-registering task definitions from cluster"
run_aws ecs list-task-definitions --family-prefix ${SERVICE_FAMILY}
task_defs=`echo ${AWS_LAST_RESULT} | jq .taskDefinitionArns[] | cut -d\" -f2 | cut -d\/ -f2`

for task_def in ${task_defs}; do
    run_aws ecs deregister-task-definition --task-definition ${task_def}
done

echo "Removing cluster ${CLUSTER_NAME}"
run_aws ecs delete-cluster --cluster ${CLUSTER_NAME}


popd