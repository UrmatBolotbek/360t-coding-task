# Player Communication Task for 360T

This project is an implementation of a coding task that demonstrates communication between two "Player" instances. The solution is provided in two distinct modes as per the requirements:

1.  **Single-Process Mode**: Both players run as threads within the same Java process and communicate using thread-safe message queues.
2.  **Multi-Process Mode**: Each player runs in a separate Java process and communicates over the network using TCP sockets.

The project is built with Java 21 and uses the Maven Wrapper, so no prior installation of Maven is required. It focuses on clean design, clear responsibilities, and modern concurrency practices.

**Author:** Urmat Bolotbek Uulu

---

## Requirements

* [Java 21 (or newer)](https://www.oracle.com/java/technologies/downloads/)
* A Unix-like shell (bash, zsh) to run the startup scripts.

---

## How to Run

First, clone or download the project and navigate to its root directory. The provided shell scripts will handle compiling and running the application using the embedded Maven Wrapper.

### 1. Single-Process Simulation

This will start a single Java application where two threads, "Initiator" and "Receiver", will exchange 10 messages before gracefully shutting down.

**To run, execute this command in your terminal:**

```bash
sh ./start_single_process.sh
```

### 2. Multi-Process Simulation

This will start two separate Java processes. The `receiver` (server) will start in the background, followed by the `initiator` (client) in the foreground. They will communicate over TCP, exchange 10 messages, and then both processes will shut down gracefully.

**To run, execute this command in your terminal:**

```bash
sh ./start_multi_process.sh
```

---

## Project Structure

```
.
├── .mvn/                     # Maven Wrapper configuration
├── mvnw                      # Maven Wrapper executable for Unix-like systems
├── mvnw.cmd                  # Maven Wrapper script for Windows
├── pom.xml                   # Maven project configuration
├── start_multi_process.sh    # Script to run the multi-process version
├── start_single_process.sh   # Script to run the single-process version
└── src
    ├── main
    │   └── java
    │       └── com
    │           └── task360T
    │               └── game 
    │                   ├── common          # Shared constants
    │                   ├── multiprocess    # Code for the multi-process solution
    │                   └── singleprocess   # Code for the single-process solution
    └── test
        └── java
            └── ...               # Unit tests
```

### Class Responsibilities

* `singleprocess.Player`: Represents a player in a single JVM, running in its own thread and using `BlockingQueue` for communication.
* `singleprocess.MainApp`: The entry point for the single-process version. It creates, links, and starts the players.
* `multiprocess.PlayerProcess`: A single class that can run in either "server" (receiver) or "client" (initiator) mode for the multi-process version. It handles communication via sockets.
* `common.Constants`: A utility class holding shared configuration values like message count and network port.