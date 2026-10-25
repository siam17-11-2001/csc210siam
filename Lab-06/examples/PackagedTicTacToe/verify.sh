#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p out
javac -d out gamelogic/*.java userinterface/*.java main/*.java test/*.java
java -cp out test.GameSelfTest
