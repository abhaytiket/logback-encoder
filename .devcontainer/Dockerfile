# Base image
FROM debian:12.5
WORKDIR /app
RUN apt-get update && apt-get install -y git curl unzip zip
RUN curl -s "https://get.sdkman.io" | bash
SHELL ["/bin/bash", "-c"]
RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk install java 21-librca && sdk install java 11.0.23-librca && sdk install maven
RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && sdk default java 21-librca
EXPOSE 1099
