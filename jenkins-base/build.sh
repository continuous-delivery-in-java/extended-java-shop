#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

pushd ${SCRIPT_DIR}

graceful_exit() {
    eval "popd; exit $1" # This ensures that popd is executed without affecting the result code of the script
}

# build image
docker build -t quiram/jenkins-base .
exit_code=$?

# show message
echo
echo "Remember to rebuild all other jenkins images based on this template container."
echo

graceful_exit ${exit_code}
