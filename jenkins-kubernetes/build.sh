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

# Copy files to connect to minikube

source_dir=$HOME/.minikube
dest_dir=kubernetes/secrets
secret_files="ca.crt client.crt client.key"

mkdir -p ${dest_dir}

for file in ${secret_files}; do
    ensure_file ${source_dir}/${file}
    cp ${source_dir}/${file} ${dest_dir}/${file}
done

# Copy IP address into kubeconfig

minikube_ip=$(minikube ip)

cat kubernetes/kubeconfig_template | sed -e s/"%MINIKUBE_IP%"/"${minikube_ip}"/ > kubernetes/kubeconfig

# build image
docker build -t quiram/jenkins-kubernetes .
exit_code=$?

# show message about not publishing this image (delete from my docker hub!)

echo
echo "The image quiram/jenkins-minikube is ready to be used, you can run it with the accompanying docker-compose.yml"
echo "WARNING: this image contains the private key to access your locally-run minikube."
echo "WARNING: it is not advisable to publish this image into a publicly-accessible docker repository."
echo

graceful_exit ${exit_code}
