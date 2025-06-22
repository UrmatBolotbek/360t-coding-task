package com.task360T.game.multiprocess;

import com.task360T.game.common.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Main application for the multi-process solution.
 * This single class can run in one of two modes: "initiator" (client) or "receiver" (server).
 * The mode is determined by command-line arguments.
 * <p>
 * Responsibilities:
 * 1.  As a Receiver (Server):
 * - Open a ServerSocket and wait for a client connection.
 * - Once connected, enter a loop to read messages and send back replies.
 * - The reply is the received message concatenated with the sent message counter.
 * - Shut down gracefully when a "STOP" command is received.
 * <p>
 * 2.  As an Initiator (Client):
 * - Connect to the receiver (server) at a specified host and port.
 * - Start the communication by sending the first message.
 * - Enter a loop to send a message and wait for a reply for a fixed number of iterations.
 * - After the final message, send a "STOP" command to the server to terminate it.
 * - Shut down gracefully.
 *
 * @author Urmat Bolotbek Uulu
 */
public class PlayerProcess {

    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            return;
        }

        String mode = args[0].toLowerCase();
        switch (mode) {
            case "initiator":
                runInitiator();
                break;
            case "receiver":
                runReceiver();
                break;
            default:
                printUsage();
        }
    }

    /**
     * Runs the process in Initiator (client) mode.
     */
    private static void runInitiator() {
        System.out.println("[Initiator] Starting...");
        // Using try-with-resources to ensure the socket and streams are closed automatically.
        try (Socket socket = new Socket(Constants.HOST, Constants.PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            System.out.printf("[Initiator] Connected to receiver at %s:%d%n", Constants.HOST, Constants.PORT);
            int messagesSentCounter = 0;
            String messageToSend = "START";

            while (messagesSentCounter < Constants.TOTAL_MESSAGES_TO_EXCHANGE) {
                messagesSentCounter++;
                System.out.printf("[Initiator] Sending message #%d: '%s'%n", messagesSentCounter, messageToSend);
                out.println(messageToSend);

                String receivedMessage = in.readLine();
                if (receivedMessage == null) {
                    System.out.println("[Initiator] Receiver closed connection unexpectedly.");
                    break;
                }
                System.out.printf("[Initiator] Received reply: '%s'%n", receivedMessage);
                
                // The next message to send is the reply we just received.
                messageToSend = receivedMessage;
            }

            // After the loop, send the stop command to the receiver.
            System.out.println("[Initiator] Reached message limit. Sending STOP command to receiver.");
            out.println(Constants.STOP_COMMAND);

        } catch (UnknownHostException e) {
            System.err.println("[Initiator] Error: Don't know about host " + Constants.HOST);
        } catch (IOException e) {
            System.err.println("[Initiator] Error: Couldn't get I/O for the connection to " + Constants.HOST + ". Is the receiver running?");
        }
        System.out.println("[Initiator] Shutting down.");
    }

    /**
     * Runs the process in Receiver (server) mode.
     */
    private static void runReceiver() {
        System.out.println("[Receiver] Starting and waiting for connection on port " + Constants.PORT);
        // Using try-with-resources for automatic resource management.
        try (ServerSocket serverSocket = new ServerSocket(Constants.PORT);
             Socket clientSocket = serverSocket.accept(); // Blocks until a client connects.
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())))
        {
            System.out.println("[Receiver] Initiator connected from " + clientSocket.getRemoteSocketAddress());
            String receivedMessage;
            int messagesSentCounter = 0;

            // Loop until the client sends a message or the stop command.
            while ((receivedMessage = in.readLine()) != null) {
                System.out.printf("[Receiver] Received message: '%s'%n", receivedMessage);

                if (Constants.STOP_COMMAND.equals(receivedMessage)) {
                    System.out.println("[Receiver] STOP command received. Shutting down.");
                    break;
                }
                
                messagesSentCounter++;
                String response = receivedMessage + " " + messagesSentCounter;
                System.out.printf("[Receiver] Sending reply #%d: '%s'%n", messagesSentCounter, response);
                out.println(response);
            }
        } catch (IOException e) {
            System.err.println("[Receiver] Error: Exception caught when trying to listen on port "
                    + Constants.PORT + " or listening for a connection.");
            System.err.println(e.getMessage());
        }
        System.out.println("[Receiver] Shutting down.");
    }
    
    /**
     * Prints the correct command-line usage for the application.
     */
    private static void printUsage() {
        System.err.println("Usage: java com.task360T.game.multiprocess.PlayerProcess <mode>");
        System.err.println("  <mode> can be 'initiator' or 'receiver'");
    }
}