#!/usr/bin/env sh
set -eu

GRADLE_BIN="/Users/laura.oran/.gradle/wrapper/dists/gradle-9.1.0-bin/9agqghryom9wkf8r80qlhnts3/gradle-9.1.0/bin/gradle"
exec "$GRADLE_BIN" "$@"
