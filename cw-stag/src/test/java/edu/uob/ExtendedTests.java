package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.Duration;

class ExtendedTests {

    private GameServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> {
                    return server.handleCommand(command);
                },
                "Server took too long to respond (probably stuck in an infinite loop)");
    }
    @Test
    void extendedTest(){
        String response = sendCommandToServer("simon: get axe");
        assertTrue(response.toLowerCase().contains("you picked up"),"");
        response = sendCommandToServer("simon: goto forest");
        assertTrue(response.toLowerCase().contains("you are now "),"");
        response = sendCommandToServer("simon: cut down the tree");
        assertTrue(response.toLowerCase().contains("you cut down "),"");
        response = sendCommandToServer("simon: get log");
        assertTrue(response.toLowerCase().contains("you picked up"),"");
        response = sendCommandToServer("simon: goto riverbank");
        assertTrue(response.toLowerCase().contains("you are now"),"");
        response = sendCommandToServer("simon: bridge over the river");
        assertTrue(response.toLowerCase().contains("you bridge the river"),"");
    }
}
