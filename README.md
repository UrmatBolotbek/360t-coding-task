# Player Communication Task for 360T

This project is an implementation of a coding task that demonstrates communication between two "Player" instances. The solution is provided in two distinct modes as per the requirements:

1. **Single-Process Mode**: Both players run as threads within the same Java process and communicate using thread-safe message queues.
2. **Multi-Process Mode**: Each player runs in a separate Java process and communicates over the network using TCP sockets.

The project is built with Java 21 and Maven. It focuses on clean design, clear responsibilities, and modern concurrency practices.

**Author:** Urmat Bolotbek Uulu

---

## Requirements

* [Java 21 (or newer)](https://www.oracle.com/java/technologies/downloads/)
* [Apache Maven](https://maven.apache.org/download.cgi)
* A Unix-like shell (bash, zsh) to run the startup scripts.

---

## How to Run

First, unzip the archive and navigate to the project's root directory. The provided shell scripts require a local installation of Maven to compile and run the application.

### 1. Single-Process Simulation

**To run, execute this command in your terminal:**

```bash
sh ./start_single_process.sh
```

### 2. Multi-Process Simulation

**To run, execute this command in your terminal:**

```bash
sh ./start_multi_process.sh
```

---

## Project Structure

```
.
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
