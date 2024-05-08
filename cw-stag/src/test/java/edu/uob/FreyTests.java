package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class FreyTests {
    private GameServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "freya-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }
    @Test
    void checkDoublePathMake(){
        String response = sendCommandToServer("simon: goto forest");
        assertTrue(response.toLowerCase().contains("you are now"),"");
        response = sendCommandToServer("simon: get key");
        assertTrue(response.toLowerCase().contains("you picked up"),"");
        response = sendCommandToServer("simon: goto cabin");
        assertTrue(response.toLowerCase().contains("you are now"),"");
        response = sendCommandToServer("simon: unlock trapdoor");
        assertTrue(response.toLowerCase().contains("you unlock"),"");
        response = sendCommandToServer("simon: unlock trapdoor");
        assertTrue(response.toLowerCase().contains("you unlock"),"");
        response = sendCommandToServer("simon: look");
        assertFalse(response.toLowerCase().contains("cellar\ncellar"),"");

    }
}
