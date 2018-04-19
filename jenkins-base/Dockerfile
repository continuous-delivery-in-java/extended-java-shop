FROM jenkins/jenkins

USER root

# Add sudo capabilities
# TODO: this is useful, but is it needed?
RUN apt-get update \
      && apt-get install -y sudo \
      && rm -rf /var/lib/apt/lists/*
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers

# Configure Docker to be auto-installed in Jenkins
COPY --chown=jenkins:jenkins docker/org.jenkinsci.plugins.docker.commons.tools.DockerTool.xml /var/jenkins_home/

# Configure Maven to be auto-installed in Jenkins
COPY --chown=jenkins:jenkins mvn/* /var/jenkins_home/

# Pre-load all necessary plugins
COPY --chown=jenkins:jenkins plugins/plugins-list /var/jenkins_home/
RUN /usr/local/bin/install-plugins.sh `cat /var/jenkins_home/plugins-list`

# Configure build all three pipelines
COPY --chown=jenkins:jenkins jobs/ /var/jenkins_home/jobs/

USER jenkins

