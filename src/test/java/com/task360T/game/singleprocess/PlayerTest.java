package com.task360T.game.singleprocess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the single-process Player communication.
 * Note: Testing concurrent applications is complex. This test verifies the final state
 * after a communication session, ensuring the message counts are correct.
 * It simulates the main application flow but within a controlled test environment.
 * @author Urmat Bolotbek Uulu
 */
class PlayerTest {

    private Player initiator;
    private Player receiver;
    private ExecutorService executor;

    /**
     * Sets up the test environment before each test case.
     * Initializes two players, links them, and prepares an executor service.
     */
    @BeforeEach
    void setUp() {
        initiator = new Player("Initiator");
        receiver = new Player("Receiver");
        initiator.setOpponent(receiver);
        receiver.setOpponent(initiator);
        executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * Tests the full communication cycle between two players.
     */
    @Test
    void testFullCommunicationCycle() throws InterruptedException {
        executor.submit(initiator);
        executor.submit(receiver);

        // Start the conversation
        initiator.sendMessage("TEST_START");
        
        // Wait for the communication to finish. We'll wait for a fixed time.
        TimeUnit.SECONDS.sleep(5); 

        executor.shutdownNow();

        assertEquals(10, initiator.getMessagesSentCounter(), "Initiator should have sent 10 messages.");
        assertEquals(10, receiver.getMessagesSentCounter(), "Receiver should have sent 10 replies.");
        assertEquals(10, initiator.getMessagesReceivedCounter(), "Initiator should have received 10 replies.");
        assertTrue(receiver.getMessagesReceivedCounter() >= 10, "Receiver should have received at least 10 messages.");
    }
}