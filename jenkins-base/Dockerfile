FROM jenkins/jenkins

USER root

# Add sudo capabilities
RUN apt-get update && apt-get install -y sudo && rm -rf /var/lib/apt/lists/*
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers

# Install vim (useful to have there)
RUN apt-get update && apt-get install -y vim

# Configure Maven to be auto-installed in Jenkins
COPY --chown=jenkins:jenkins mvn/* /var/jenkins_home/

# Easy access to maven in command-line (once installed)
RUN ln -s /var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/Default/bin/mvn /usr/bin/mvn

# Configure Docker to be auto-installed in Jenkins
COPY --chown=jenkins:jenkins docker/org.jenkinsci.plugins.docker.commons.tools.DockerTool.xml /var/jenkins_home/

# Easy access to docker in command-line (once installed)
RUN ln -s /var/jenkins_home/tools/org.jenkinsci.plugins.docker.commons.tools.DockerTool/Default/bin/docker /usr/bin/docker

# Install Docker Compose
RUN curl -L https://github.com/docker/compose/releases/download/1.21.0/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
RUN chmod a+x /usr/local/bin/docker-compose

# Pre-load all necessary plugins
COPY --chown=jenkins:jenkins plugins/plugins-list /var/jenkins_home/
RUN /usr/local/bin/install-plugins.sh `cat /var/jenkins_home/plugins-list`

# Configure build all three pipelines
COPY --chown=jenkins:jenkins jobs/ /var/jenkins_home/jobs/

# Relax security rules to be able to display Serenity BDD reports correctly
ENV JAVA_OPTS -Dhudson.model.DirectoryBrowserSupport.CSP=\"\"

USER jenkins
