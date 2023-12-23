#!/bin/bash

# Define the name of your Java process
JAVA_PROCESS_NAME="fishpond_app"

# Function to check if the process is running
is_process_running() {
    pgrep -f "$JAVA_PROCESS_NAME" >/dev/null 2>&1
}

# Function to compile Java files
compile_java() {
    javac -cp .:services/json-simple-1.1.jar services/*.java 2> compile_log.txt
}

# Function to start the Java program
start_java() {
    java -cp .:services/json-simple-1.1.jar main
}

# Check if the process is already running
if is_process_running; then
    echo "$JAVA_PROCESS_NAME is already running."
else
    echo "$JAVA_PROCESS_NAME is not running. Starting..."
    compile_java  # Compile Java files
    if [ $? -eq 0 ]; then
        start_java  # Start the Java program
    else
        echo "Compilation failed. Check compile_log.txt for details."
    fi
fi

# Monitoring loop
CHECK_INTERVAL=10

while true; do
    sleep "$CHECK_INTERVAL"

    if ! is_process_running; then
        echo "$JAVA_PROCESS_NAME is not running. Restarting..."
        compile_java  # Compile Java files
        if [ $? -eq 0 ]; then
            start_java  # Start the Java program
        else
            echo "Compilation failed. Check compile_log.txt for details."
        fi
    fi
done
