#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

JAR="lib/junit-platform-console-standalone-1.11.3.jar"

# Download the JUnit console jar from Maven Central on first run (kept in lib/).
mkdir -p lib
[ -f "$JAR" ] || curl --fail --location --show-error --silent -o "$JAR" "https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.3/junit-platform-console-standalone-1.11.3.jar"

mkdir -p out/main out/test out/myprog
mkdir -p tmp

# Compile main
find src/main -name "*.java" > ./tmp/main_sources.txt
javac -d out/main @./tmp/main_sources.txt

# Compile myprog
find src/myprog -name "*.java" > ./tmp/myprog_sources.txt
javac -cp out/main -d out/myprog @./tmp/myprog_sources.txt

# Compile tests (need junit api on classpath)
find src/test -name "*.java" > ./tmp/test_sources.txt
javac -cp "out/main:$JAR" -d out/test @./tmp/test_sources.txt

echo "Compiled main -> out/main, tests -> out/test"

