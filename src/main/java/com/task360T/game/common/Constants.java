package com.task360T.game.common;

/**
 * Shared constants for both single-process and multi-process solutions.
 * This class centralizes configuration values to ensure consistency and ease of modification.
 * @author Urmat Bolotbek Uulu
 */
public final class Constants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Constants() {}

    /**
     * The total number of messages the initiator should send before the program terminates.
     * This condition is used to gracefully stop the communication loop.
     */
    public static final int TOTAL_MESSAGES_TO_EXCHANGE = 10;

    /**
     * The network port to be used for the multi-process socket communication.
     * The receiver (server) will listen on this port, and the initiator (client) will connect to it.
     */
    public static final int PORT = 8080;

    /**
     * The hostname for the multi-process communication. "localhost" is used as both
     * processes are expected to run on the same machine.
     */
    public static final String HOST = "localhost";

    /**
     * A special message used in the multi-process solution to signal the receiver (server)
     * that the communication is complete, and it should shut down gracefully.
     */
    public static final String STOP_COMMAND = "STOP";
}