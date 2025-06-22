#!/bin/bash
# A script to compile and run the multi-process player communication application.
#
# Author: Urmat Bolotbek Uulu

set -e

echo "--- Compiling Project (skipping tests) ---"
./mvnw clean package -DskipTests -q

# Define the main class for the multi-process application
MAIN_CLASS="com.task360T.game.multiprocess.PlayerProcess"

echo ""
echo "--- Starting Multi-Process Application ---"

# Start the Receiver (Server) process in the background
echo "[Launcher] Starting Receiver process in the background..."
./mvnw exec:java -Dexec.mainClass="$MAIN_CLASS" -Dexec.args="receiver" -DskipTests &

# Capture the Process ID (PID) of the background server process
SERVER_PID=$!
echo "[Launcher] Receiver started with PID: $SERVER_PID"

trap "echo '[Launcher] Script interrupted. Killing receiver process $SERVER_PID...'; kill $SERVER_PID" SIGINT SIGTERM

sleep 2

# Start the Initiator (Client) process in the foreground
echo "[Launcher] Starting Initiator process..."
./mvnw exec:java -Dexec.mainClass="$MAIN_CLASS" -Dexec.args="initiator" -DskipTests

# Wait for the background receiver process to terminate.
echo "[Launcher] Initiator finished. Waiting for receiver process to shut down..."
wait $SERVER_PID

echo "[Launcher] Receiver process has shut down. All processes finished."
echo "-----------------------------------------"