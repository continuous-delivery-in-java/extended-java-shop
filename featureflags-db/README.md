Feature Flags Database
======================

This is a dockerised MySQL database in lieu of a real production database for Feature Flags Service to connect in production.

This is done this way to simplify the demonstration of the different deployment environments: while different technologies
will handle databases differently, docker containers are handled the same way. This way we can connect the Feature Flags
to a "real" MySQL database without having to deal with the different ways in which databases are managed in Kubernetes,
AWS, etc.