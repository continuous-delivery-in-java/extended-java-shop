#!/bin/bash

cd shopfront
mvn clean install
if docker build -t quiram/djshopfront . ; then
  docker push quiram/djshopfront
fi
cd ..

cd productcatalogue
mvn clean install
if docker build -t quiram/djproductcatalogue . ; then
  docker push quiram/djproductcatalogue
fi
cd ..

cd stockmanager
mvn clean install
if docker build -t quiram/djstockmanager . ; then
  docker push quiram/djstockmanager
fi
cd ..
