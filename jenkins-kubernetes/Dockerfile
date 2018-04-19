FROM quiram/jenkins-base

USER root

# Load plugin for kubernetes deployment
RUN /usr/local/bin/install-plugins.sh kubernetes-cd

# Override deploy job to use Kubernetes
COPY --chown=jenkins:jenkins jobs/ /var/jenkins_home/jobs/

# Configure kubernetes client
RUN mkdir /var/jenkins_home/kubernetes
COPY --chown=jenkins:jenkins kubernetes/ /var/jenkins_home/kubernetes/
COPY --chown=jenkins:jenkins credentials/credentials.xml /var/jenkins_home/

USER jenkins

