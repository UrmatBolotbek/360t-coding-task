#!/bin/bash
set -e
echo "--- Compiling Project (skipping tests) ---"
mvn clean package -DskipTests -q
MAIN_CLASS="com.task360T.game.multiprocess.PlayerProcess"
echo ""
echo "--- Starting Multi-Process Application ---"
echo "[Launcher] Starting Receiver process in the background..."
mvn exec:java -Dexec.mainClass="$MAIN_CLASS" -Dexec.args="receiver" -DskipTests &
SERVER_PID=$!
echo "[Launcher] Receiver started with PID: $SERVER_PID"
trap "echo '[Launcher] Script interrupted. Killing receiver process $SERVER_PID...'; kill $SERVER_PID" SIGINT SIGTERM
sleep 2
echo "[Launcher] Starting Initiator process..."
mvn exec:java -Dexec.mainClass="$MAIN_CLASS" -Dexec.args="initiator" -DskipTests
echo "[Launcher] Initiator finished. Waiting for receiver process to shut down..."
wait $SERVER_PID
echo "[Launcher] Receiver process has shut down. All processes finished."
echo "-----------------------------------------"