FROM quiram/jenkins-base

USER root

# Install PIP
RUN curl -O https://bootstrap.pypa.io/get-pip.py
RUN python get-pip.py

# Install latest AWS Client
RUN pip install awscli --upgrade

# Install jq (used by deployment script)
RUN apt-get update && sudo apt-get install -y jq

USER jenkins
