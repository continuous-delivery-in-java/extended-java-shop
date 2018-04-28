#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

pushd ${SCRIPT_DIR}
source ./constants.sh

# Create cluster
echo "Creating cluster with name ${CLUSTER_NAME}"
run_aws ecs create-cluster --cluster-name ${CLUSTER_NAME}

# Create key pair
echo "Creating key pair with name ${KEY_PAIR_NAME}"
run_aws ec2 create-key-pair --key-name ${KEY_PAIR_NAME}
echo ${AWS_LAST_RESULT} | jq .KeyMaterial | cut -d\" -f2 | awk '{gsub(/\\n/,"\n")}1' >${PRIVATE_KEY_FILE}
chmod 400 ${PRIVATE_KEY_FILE}
echo "Private key for key pair ${KEY_PAIR_NAME} available at `pwd`/${PRIVATE_KEY_FILE}"
echo "For your convenience, permissions to this file have been set to 400"

# Create security group
echo "Creating security group ${SECURITY_GROUP_NAME}"
run_aws ec2 create-security-group \
    --description '"Security group to access all containers within the Extended Java Shop"' \
    --group-name ${SECURITY_GROUP_NAME}

# Add rules to security group
echo "Adding rules to security group ${SECURITY_GROUP_NAME}"
add_ingress_rule ${SECURITY_GROUP_NAME} 22
add_ingress_rule ${SECURITY_GROUP_NAME} 8000-8100
add_ingress_rule ${SECURITY_GROUP_NAME} 5432

# Create IAM roles
#create_iam_role ${ECS_SERVICE_ROLE} ${EC2_CONTAINER_SERVICE_ARN} '"Allows ECS to (de)register EC2 instances in and out of load balancers."'
create_iam_role ${ECS_INSTANCE_ROLE} ${EC2_FOR_ECS_POLICY_ARN} '"Allows EC2 instances in an ECS cluster to access ECS."'

# AWS automatically creates an instance profile when you create a role, with the same name, but it doesn't link the two...
echo "Adding role '${ECS_INSTANCE_ROLE}' to the relevant instance profile"
run_aws iam add-role-to-instance-profile \
    --role-name ${ECS_INSTANCE_ROLE} \
    --instance-profile-name ${ECS_INSTANCE_ROLE}

echo "Creating private DNS namespace '${DNS_NAMESPACE}' for service discovery"
# Get current VPC id
run_aws ec2 describe-vpcs
vpc_id=`echo ${AWS_LAST_RESULT} | jq .Vpcs[].VpcId | cut -d\" -f2`

echo "Creating private namespace '${DNS_NAMESPACE}'"
run_aws servicediscovery create-private-dns-namespace \
    --name ${DNS_NAMESPACE} \
    --vpc ${vpc_id}



# Create temporary file for cluster attachment
TEMP=".tmp.attachment.sh"

cat >${TEMP} <<EOF
#!/usr/bin/env bash
echo ECS_CLUSTER=${CLUSTER_NAME} >> /etc/ecs/ecs.config
EOF

# Create instances
echo "Creating ${NUMBER_OF_EC2_INSTANCES} EC2 instances that will be added to your ECS cluster"
run_aws ec2 run-instances \
    --image-id ${AMI_ID} \
    --count ${NUMBER_OF_EC2_INSTANCES} \
    --instance-type t2.micro \
    --key-name ${KEY_PAIR_NAME} \
    --security-groups "${SECURITY_GROUP_NAME}" \
    --user-data file://${TEMP} \
    --iam-instance-profile Name=${ECS_INSTANCE_ROLE} \
    --tag-specifications "'ResourceType=instance,Tags=[{Key=${TAG_KEY},Value=${TAG_VALUE}}]'"

rm -f ${TEMP}

echo ""
echo "All done. Your cluster should be ready at https://${REGION}.console.aws.amazon.com/ecs/home?region=${REGION}#/clusters/${CLUSTER_NAME}/services"

popd
