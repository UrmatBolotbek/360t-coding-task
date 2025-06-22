package com.task360T.game.singleprocess;

import com.task360T.game.common.Constants;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Represents a Player in the single-process communication simulation.
 * Each Player instance runs in its own thread and communicates with another Player
 * instance via thread-safe message queues.
 * <p>
 * Responsibilities:
 * 1.  Run as an independent task (implements Runnable).
 * 2.  Maintain a thread-safe queue for incoming messages.
 * 3.  Hold a reference to an opponent Player to send messages to.
 * 4.  Process received messages and send back a reply, concatenating the original message
 * with a counter of sent messages.
 * 5.  Track the number of messages sent and received.
 * 6.  Contribute to the graceful shutdown of the application once the communication limit is reached.
 *
 * @author Urmat Bolotbek Uulu
 */
public class Player implements Runnable {

    private final String name;
    private final BlockingQueue<String> incomingMessages = new LinkedBlockingQueue<>();
    private Player opponent;
    private int messagesSentCounter = 0;
    private int messagesReceivedCounter = 0;
    private volatile boolean running = true;

    /**
     * Constructs a new Player with a given name.
     * @param name The name of the player, for logging and identification.
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Sets the opponent player. This method must be called before starting the threads
     * to establish the communication link.
     * @param opponent The other Player instance.
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    /**
     * The main execution logic for the player's thread.
     * The player runs in a loop, waiting for incoming messages from its queue.
     * When a message is received, it processes it, sends a reply, and checks
     * if the termination condition is met.
     */
    @Override
    public void run() {
        while (running) {
            try {
                // Blocks until a message is available in the queue.
                String receivedMessage = incomingMessages.take();
                messagesReceivedCounter++;
                System.out.printf("[%s] Received message #%d: '%s'%n", name, messagesReceivedCounter, receivedMessage);

                // Stop this player if the opponent has already initiated shutdown
                if (Constants.STOP_COMMAND.equals(receivedMessage)) {
                    this.stop();
                    continue; // Exit the loop
                }

                if ("Initiator".equals(name) && messagesReceivedCounter == Constants.TOTAL_MESSAGES_TO_EXCHANGE) {
                    System.out.printf("%n[%s] has now sent %d messages and received %d replies. Mission complete. Shutting down.%n",
                            name, messagesSentCounter, messagesReceivedCounter);
                    // Signal the other player to stop.
                    opponent.sendMessage(Constants.STOP_COMMAND);
                    this.stop();
                    // Gracefully exit the entire JVM.
                    System.exit(0);
                    continue; // This is important to not proceed to send another message
                }

                // Create and send a response.
                String response = receivedMessage + " " + messagesSentCounter;
                this.sendMessage(response);

            } catch (InterruptedException e) {
                System.err.printf("[%s] was interrupted. Shutting down.%n", name);
                this.running = false; // Ensure loop terminates
                Thread.currentThread().interrupt(); // Preserve the interrupted status
            }
        }
        System.out.printf("[%s] has stopped.%n", name);
    }

    /**
     * Sends a message to the opponent player by adding it to their incoming message queue.
     * This method is thread-safe because it uses a BlockingQueue.
     * @param message The message content to send.
     */
    public void sendMessage(String message) {
        if (opponent != null) {
            try {
                // Don't increment counter for the final STOP message
                if (!Constants.STOP_COMMAND.equals(message)) {
                    messagesSentCounter++;
                }
                System.out.printf("[%s] Sending message: '%s' to %s%n", name, message, opponent.name);
                opponent.incomingMessages.put(message);
            } catch (InterruptedException e) {
                System.err.printf("[%s] was interrupted while sending a message.%n", name);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Signals the player to stop its execution loop.
     */
    private void stop() {
        this.running = false;
    }

    // --- Getters for testing purposes ---

    public int getMessagesSentCounter() {
        return messagesSentCounter;
    }

    public int getMessagesReceivedCounter() {
        return messagesReceivedCounter;
    }
}
