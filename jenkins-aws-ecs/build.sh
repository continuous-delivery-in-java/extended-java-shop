#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

pushd ${SCRIPT_DIR}

graceful_exit() {
    eval "popd; exit $1" # This ensures that popd is executed without affecting the result code of the script
}

no_file_error_message() {
    file_name=$1

    echo "${file_name} file not found"
    echo "You need to run minikube first to generate secret files."

    graceful_exit -1
}

ensure_file() {
    file_name=$1

    [ -f ${file_name} ] || no_file_error_message ${file_name}
}

# build image
docker build -t quiram/jenkins-aws-ecs .
exit_code=$?

graceful_exit ${exit_code}
