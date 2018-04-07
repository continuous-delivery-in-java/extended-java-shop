#!/usr/bin/env bash

build_and_publish() {
    image_name=$1
    cd ${image_name}
    if docker build -t quiram/${image_name} . ; then
      docker push quiram/${image_name}
    fi
    cd ..
}

build_all() {
    for item in $*; do
        build_and_publish ${item}
    done
}

build_all jenkins-base

