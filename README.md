# Extended Java Shop
This repo contains code samples for the book ["Continuous Delivery in Java"](http://shop.oreilly.com/product/0636920078777.do). It started as a fork of [Daniel Bryant](https://github.com/danielbryantuk)'s [O'Reilly Docker Java Shopping repository](https://github.com/danielbryantuk/oreilly-docker-java-shopping), but it evolved to become a repository on its own right.

This README is intended to provide high-level guidance of the project, and detailed instructions can be found in the accompanying book.

## Project Structure

Further instructions may exist within the following sub-folders.

* acceptance-tests
    * Simple examples of functional end-to-end tests that use JUnit, [REST-assured](http://rest-assured.io/), and SerenityBDD to test the entire set of services comprising the Extended Java Shop.
* external-adaptive-pricing
    * Service that represents some adaptive pricing service provided by a supposed third party.
* fake-adaptive-pricing
    * The fake service that a team would use for tests, instead of the real external-adaptive-pricing service.
* featureflags
    * The Feature Flags Service that is used within the Extended Java Shop to decide which features should be enabled and to what degree.
* featureflags-db
    * The DB used by the Feature Flags Service. In this case it will be just a Postgre DB in a Docker container, in a real-life situation it would be a proper database.
* jenkins-aws-ecs
    * A pre-built Jenkins instance that deploys to a known AWS ECS Cluster; this is based on jenkins-base.
* jenkins-base
    * A pre-built Jenkins instance with job definitions for all the services and tests. It includes an empty "deploy" job which doesn't do anything; this job is overridden at `jenkins-kubernetes` and `jenkins-aws-ecs` to deploy to the right location.
* jenkins-kubernetes
    * A pre-built Jenkins instance that deploys to a locally running Kubernetes Cluster; this is based on jenkins-base. 
* productcatalogue
    * The Product Catalogue Service, which provides product details like name and price.
* shopfront
    * The Shopfront Service that provides the primary entry point for the end-user (both Web UI and API-driven).
* stockmanager
    * The Stock Manager Service, which provides stock information, such as SKU and available quantity.
* test-featureflags-db
    * The DB used by Feature Flags Service in the test environment. In this case, like featureflags-db, it's just a Postgre DB in a Docker container, although in a real-life situation it would probably be a proper database. 
