#!/bin/sh
# Gradle wrapper script placeholder
# Download gradle-wrapper.jar from: https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar
# Place it in: gradle/wrapper/gradle-wrapper.jar
# Then this script will work

if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "ERROR: gradle/wrapper/gradle-wrapper.jar not found"
    echo ""
    echo "To fix:"
    echo "  wget https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar -O gradle/wrapper/gradle-wrapper.jar"
    echo ""
    echo "OR use Android Studio to build this project"
    exit 1
fi

# Standard Gradle wrapper execution
exec java -jar gradle/wrapper/gradle-wrapper.jar "$@"
