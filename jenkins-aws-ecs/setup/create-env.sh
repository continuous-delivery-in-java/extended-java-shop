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
run_aws ec2 authorize-security-group-ingress \
    --group-name ${SECURITY_GROUP_NAME} \
    --protocol tcp --port 22 --cidr 0.0.0.0/0

run_aws ec2 authorize-security-group-ingress \
    --group-name ${SECURITY_GROUP_NAME} \
    --protocol tcp --port 8000-8100 --cidr 0.0.0.0/0

run_aws ec2 authorize-security-group-ingress \
    --group-name ${SECURITY_GROUP_NAME} \
    --protocol tcp --port 5432 --cidr 0.0.0.0/0

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

# Create IAM role to allow the ECS agents in the instances register with the cluster
echo "Creating IAM role to link ECS cluster with EC2 instances"
run_aws iam create-role \
    --role-name ${ECS_INSTANCE_ROLE} \
    --description '"Allows EC2 instances in an ECS cluster to access ECS."' \
    --assume-role-policy-document file://${TEMP}

rm -f ${TEMP}

# AWS automatically creates an instance profile when you create a role, with the same name, but it doesn't link the two...
run_aws iam add-role-to-instance-profile \
    --role-name ${ECS_INSTANCE_ROLE} \
    --instance-profile-name ${ECS_INSTANCE_ROLE}

run_aws iam attach-role-policy \
    --policy-arn ${EC2_FOR_ECS_POLICY_ARN} \
    --role-name ${ECS_INSTANCE_ROLE}

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
    --iam-instance-profile Name=${ECS_INSTANCE_ROLE}

rm -f ${TEMP}

popd
