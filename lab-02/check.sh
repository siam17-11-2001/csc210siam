#!/usr/bin/env bash
# Lab 02 DNA smoke test: compile, run, and check the expected results.
set -u
cd "$(dirname "$0")" || exit 1

if ! command -v javac >/dev/null 2>&1 || ! command -v java >/dev/null 2>&1; then
    echo "FAIL: Java JDK is required (javac and java)." >&2
    exit 1
fi

if ! javac DNAComparison.java; then
    echo "FAIL: DNAComparison.java did not compile." >&2
    exit 1
fi

if ! output=$(java DNAComparison); then
    echo "FAIL: DNAComparison did not run." >&2
    exit 1
fi

failures=0
check_line() {
    local expected="$1"
    if printf '%s\n' "$output" | grep -Fxq "$expected"; then
        echo "PASS: $expected"
    else
        echo "FAIL: Expected $expected"
        failures=$((failures + 1))
    fi
}

# Reference translation of the three DNA strands in the assignment.
check_line 'DNA1 amino acids: [L, I, L, Y, P, A, D]'
check_line 'DNA2 amino acids: [L, A, G, G, Y, Stop, Stop, Stop, T, I, P]'
check_line 'DNA3 amino acids: [L, I, L, Y, P, A, D]'
check_line 'DNA1 and DNA2 identical: false'
check_line 'DNA1 and DNA3 identical: true'
check_line 'DNA2 and DNA3 identical: false'

if (( failures > 0 )); then
    echo "Smoke test failed: $failures check(s)."
    exit 1
fi
echo 'All 6 DNA checks passed.'
