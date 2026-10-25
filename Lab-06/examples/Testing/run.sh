#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
if [ ! -d out/main ]; then
    ./compile.sh
fi
java -cp out/myprog:out/main com.myprog.main.Main
