package com.task360T.game.singleprocess;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.task360T.game.common.Constants;

/**
 * Main application entry point for the single-process solution.
 * This class is responsible for creating, linking, and running the two players
 * within the same Java Virtual Machine.
 * @author Urmat Bolotbek Uulu
 */
public class MainApp {

    /**
     * The main method that sets up and executes the player communication simulation.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Using a virtual thread per task executor is a modern and efficient approach for I/O-bound tasks.
        // It's lightweight and perfect for this kind of message-passing simulation.
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        System.out.println("Single-Process Communication Simulation");
        System.out.println("========================================");
        System.out.printf("Initiator will send %d messages in total.%n%n", Constants.TOTAL_MESSAGES_TO_EXCHANGE);

        // Create the two players
        Player initiator = new Player("Initiator");
        Player receiver = new Player("Receiver");

        // Link players to each other so they can communicate
        initiator.setOpponent(receiver);
        receiver.setOpponent(initiator);

        // Assign the communication task for each player to the executor
        executor.submit(initiator);
        executor.submit(receiver);

        // The initiator starts the conversation by sending the first message.
        // This is done after both players are running in their threads.
        initiator.sendMessage("START");

        // The ExecutorService needs to be shut down gracefully.
        // We will wait for the tasks to complete. The players themselves will handle
        // the termination logic. The shutdown process here is a safeguard.
        try {
            // Initiator's logic will trigger system exit, but as a fallback,
            // we can shut down the executor after a timeout.
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("Executor timed out. Forcing shutdown.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted. Forcing shutdown.");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}