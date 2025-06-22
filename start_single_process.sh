#!/bin/bash
# A script to compile and run the single-process player communication application.
#
# Author: Urmat Bolotbek Uulu

set -e

echo "--- Compiling Project (skipping tests) ---"
# We skip tests here because the application's test calls System.exit(),
# which would fail the build. Tests can be run separately via `mvnw test`.
./mvnw clean package -DskipTests -q

# Define the main class to be executed
MAIN_CLASS="com.task360T.game.singleprocess.MainApp"

echo ""
echo "--- Running Single-Process Application ---"
echo "Main class: $MAIN_CLASS"
echo "------------------------------------------"

# Execute the main class
./mvnw exec:java -Dexec.mainClass="$MAIN_CLASS" -Dexec.cleanupDaemonThreads=false -DskipTests