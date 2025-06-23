#!/bin/bash
set -e
echo "--- Compiling Project (skipping tests) ---"
mvn clean package -DskipTests -q
MAIN_CLASS="com.task360T.game.singleprocess.MainApp"
echo ""
echo "--- Running Single-Process Application ---"
echo "Main class: $MAIN_CLASS"
echo "------------------------------------------"
mvn exec:java -Dexec.mainClass="$MAIN_CLASS" -Dexec.cleanupDaemonThreads=false -DskipTests