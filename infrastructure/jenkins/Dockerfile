FROM jenkins/jenkins:lts

USER root

# ─────────────────────────────────────────────
# Install system dependencies
# ─────────────────────────────────────────────
RUN apt-get update && apt-get install -y \
    docker.io \
    maven \
    curl \
    wget \
    git \
    apt-transport-https \
    ca-certificates \
    gnupg \
    lsb-release \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# ─────────────────────────────────────────────
# Install kubectl (latest stable)
# ─────────────────────────────────────────────
RUN curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" \
    && install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl \
    && rm kubectl

# ─────────────────────────────────────────────
# Install Node.js 20 (needed for frontend builds)
# ─────────────────────────────────────────────
RUN curl -fsSL https://deb.nodesource.com/setup_20.x | bash - \
    && apt-get install -y nodejs \
    && rm -rf /var/lib/apt/lists/*

# ─────────────────────────────────────────────
# Add jenkins user to docker group
# Stay as root for Docker socket access on Windows/Docker Desktop
# ─────────────────────────────────────────────
RUN usermod -aG docker jenkins

# ─────────────────────────────────────────────
# Pre-install Jenkins plugins
# ─────────────────────────────────────────────
RUN jenkins-plugin-cli --plugins \
    git \
    workflow-aggregator \
    docker-workflow \
    sonar \
    junit \
    pipeline-stage-view \
    blueocean \
    credentials-binding \
    timestamper \
    ws-cleanup

# ─────────────────────────────────────────────
# Verify installations
# ─────────────────────────────────────────────
RUN docker --version \
    && mvn --version \
    && kubectl version --client \
    && node --version \
    && npm --version \
    && git --version