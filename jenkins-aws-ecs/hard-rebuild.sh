#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

pushd ${SCRIPT_DIR}

graceful_exit() {
    eval "popd; exit $1" # This ensures that popd is executed without affecting the result code of the script
}


docker system prune && docker volume rm $(docker volume ls -qf dangling=true)

pushd ../jenkins-base
./build.sh && ./push.sh
popd
./build.sh

graceful_exit $?
