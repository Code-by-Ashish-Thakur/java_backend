#!/bin/sh
# Maven Wrapper script - downloads Maven automatically if not present
# Usage: ./mvnw spring-boot:run

set -e

MAVEN_WRAPPER_JAR=".mvn/wrapper/maven-wrapper.jar"
WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
DIST_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip"
MAVEN_HOME="${HOME}/.m2/wrapper/dists/apache-maven-3.9.6"

if [ ! -d "$MAVEN_HOME" ]; then
    echo "Downloading Maven 3.9.6..."
    mkdir -p "$MAVEN_HOME"
    if command -v curl > /dev/null; then
        curl -fsSL "$DIST_URL" -o "/tmp/maven.zip"
    elif command -v wget > /dev/null; then
        wget -q "$DIST_URL" -O "/tmp/maven.zip"
    fi
    unzip -q /tmp/maven.zip -d "$MAVEN_HOME"
    rm /tmp/maven.zip
fi

MAVEN_BIN=$(find "$MAVEN_HOME" -name "mvn" -path "*/bin/mvn" | head -1)
exec "$MAVEN_BIN" "$@"
