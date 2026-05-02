FROM jenkins/jenkins:lts

USER root

# Install Docker CLI, Maven, curl, and kubectl
RUN apt-get update && apt-get install -y \
    docker.io \
    maven \
    curl \
    apt-transport-https \
    ca-certificates \
    gnupg \
    && rm -rf /var/lib/apt/lists/*

# Install kubectl
RUN curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" \
    && install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl \
    && rm kubectl

# Add jenkins user to docker group
RUN usermod -aG docker jenkins

# Stay as root for Docker socket access on Windows/Docker Desktop

# Pre-install useful Jenkins plugins
RUN jenkins-plugin-cli --plugins \
    git \
    workflow-aggregator \
    docker-workflow \
    sonar \
    junit \
    pipeline-stage-view \
    blueocean