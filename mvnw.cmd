@REM Maven Wrapper script for Windows
@REM Usage: mvnw.cmd spring-boot:run

@echo off
setlocal

set "MAVEN_VERSION=3.9.6"
set "DIST_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip"
set "MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%"

if not exist "%MAVEN_HOME%\apache-maven-%MAVEN_VERSION%\bin\mvn.cmd" (
    echo Downloading Maven %MAVEN_VERSION%...
    mkdir "%MAVEN_HOME%" 2>nul
    powershell -Command "Invoke-WebRequest -Uri '%DIST_URL%' -OutFile '%TEMP%\maven.zip'"
    powershell -Command "Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%MAVEN_HOME%' -Force"
    del "%TEMP%\maven.zip"
)

"%MAVEN_HOME%\apache-maven-%MAVEN_VERSION%\bin\mvn.cmd" %*
