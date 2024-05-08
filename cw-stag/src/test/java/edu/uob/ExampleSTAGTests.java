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

class ExampleSTAGTests {

  private GameServer server;

  // Create a new server _before_ every @Test
  @BeforeEach
  void setup() {
      File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
      File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
      server = new GameServer(entitiesFile, actionsFile);
  }

  String sendCommandToServer(String command) {
      // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
      return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
      "Server took too long to respond (probably stuck in an infinite loop)");
  }

  // A lot of tests will probably check the game state using 'look' - so we better make sure 'look' works well !
  @Test
  void testLook() {
    String response = sendCommandToServer("simon: look");
    response = response.toLowerCase();
    assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
    assertTrue(response.contains("log cabin"), "Did not see a description of the room in response to look");
    assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
    assertTrue(response.contains("forest"), "Did not see available paths in response to look");
  }

  // Test that we can pick something up and that it appears in our inventory
  @Test
  void testGet()
  {
      String response;
      sendCommandToServer("simon: get potion");
      response = sendCommandToServer("simon: inv");
      response = response.toLowerCase();
      assertTrue(response.contains("potion"), "Did not see the potion in the inventory after an attempt was made to get it");
      response = sendCommandToServer("simon: look");
      response = response.toLowerCase();
      assertFalse(response.contains("potion"), "Potion is still present in the room after an attempt was made to get it");
  }
  // Test that we can goto a different location (we won't get very far if we can't move around the game !)
  @Test
  void testGoto()
  {
      sendCommandToServer("simon: goto forest");
      String response = sendCommandToServer("simon: look");
      response = response.toLowerCase();
      assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
  }
  @Test
  void testDrop(){
      String response = sendCommandToServer("simon: get axe");
      assertTrue(response.toLowerCase().contains("picked"),"was not able to get the axe");
      response = sendCommandToServer("simon: drop axe");
      assertTrue(response.toLowerCase().contains("dropped"), "was not able to drop axe");
      response = sendCommandToServer("simon: drop axe");
      assertFalse(response.toLowerCase().contains("dropped"), "succesfully dropped nonexistant axe");
      response = sendCommandToServer("simon: drop tree");
      assertFalse(response.toLowerCase().contains("dropped"), "succesfully dropped item that isnt in scope");
      response = sendCommandToServer("simon: drop blah");
      assertFalse(response.toLowerCase().contains("dropped"), "succesfully dropped an item that doesnt exist");
  }
  @Test
  void playGame(){
      String response = sendCommandToServer("simon: get axe");
      assertTrue(response.toLowerCase().contains("picked"),"was not able to get the axe");
      response = sendCommandToServer("simon: get potion");
      assertTrue(response.toLowerCase().contains("picked"),"was not able to get the potion");
      response = sendCommandToServer("simon: inv");
      assertTrue(response.toLowerCase().contains("axe"),"was not able to get the axe");
      assertTrue(response.toLowerCase().contains("potion"),"was not able to get the potion");
      response = sendCommandToServer("simon: look");
      assertFalse(response.toLowerCase().contains("axe"),"axe is still in location");
      assertFalse(response.toLowerCase().contains("potion"),"potion is still in location");
      response = sendCommandToServer("simon: goto forest");
      assertTrue(response.toLowerCase().contains("forest"),"was not able to get to the forest");
      response = sendCommandToServer("simon: get key");
      assertTrue(response.toLowerCase().contains("picked"),"was not able to get the key");
      response = sendCommandToServer("simon: please chop down the tree");
      assertTrue(response.toLowerCase().contains("tree"),"was not able to chop the tree down");
      response = sendCommandToServer("simon: get the log");
      assertTrue(response.toLowerCase().contains("log"),"was not able to pick up the log");
      response = sendCommandToServer("simon: look");
      assertFalse(response.toLowerCase().contains("tree"),"the tree is still there");
      assertFalse(response.toLowerCase().contains("key"),"the key is still there");
      response = sendCommandToServer("simon: goto cabin");
      assertTrue(response.toLowerCase().contains("cabin"),"was not able to get to the cabin");
      response = sendCommandToServer("simon: unlock the trapdoor");
      assertTrue(response.toLowerCase().contains("unlock the trapdoor"),"was not able to unlock the trapdoor");
      response = sendCommandToServer("simon: goto cellar");
      assertTrue(response.toLowerCase().contains("cellar"),"was not able to goto the cellar");
      response = sendCommandToServer("simon: look");
      assertTrue(response.toLowerCase().contains("elf"),"the elf is  not here for some reason?/?");
      response = sendCommandToServer("simon: attack the ugly elf");
      assertTrue(response.toLowerCase().contains("he fights back"),"could not attack the elf..");
      response = sendCommandToServer("simon: drink the potion");
      assertTrue(response.toLowerCase().contains("improves"),"drinking the potion failed");
      response = sendCommandToServer("simon: what is my health score?");
      assertTrue(response.toLowerCase().contains("3"),"drinking the potion failed");
      response = sendCommandToServer("simon: hit the elf");
      assertTrue(response.toLowerCase().contains("he fights back"),"could not attack the elf..");
      response = sendCommandToServer("simon: attack the elf");
      assertTrue(response.toLowerCase().contains("he fights back"),"could not attack the elf..");
      response = sendCommandToServer("simon: get elf");
      assertFalse(response.toLowerCase().contains("you picked up"),"");
      response = sendCommandToServer("simon: fight the stupid fucking elf");
      assertTrue(response.toLowerCase().contains("died"),"you did not die ?");
      response = sendCommandToServer("simon: look");
      assertTrue(response.toLowerCase().contains("cabin"),"you are not in the cabin for some reason");
      response = sendCommandToServer("simon: inv");
      assertFalse(response.toLowerCase().contains("axe"),"you did not drop the axe on death");
      assertFalse(response.toLowerCase().contains("log"),"you did not drop the log on death");
      response = sendCommandToServer("simon: health");
      assertTrue(response.toLowerCase().contains("3"),"you are not back to full health");
      response = sendCommandToServer("simon: goto cellar");
      assertTrue(response.toLowerCase().contains("cellar"),"the cellar is not accessible");
      response = sendCommandToServer("simon: look");
      assertTrue(response.toLowerCase().contains("axe"),"the axe did not drop on the floor");
      assertTrue(response.toLowerCase().contains("log"),"the log did not drop on the floor");
  }
  @Test
  void semanticTesting(){
      String response = sendCommandToServer("simon: inv");
      assertTrue(response.toLowerCase().contains("the items in your inventory are as follows"),"");
      response = sendCommandToServer("simon: inventory");
      assertTrue(response.toLowerCase().contains("the items in your inventory are as follows"),"");
      response = sendCommandToServer("simon: InV");
      assertTrue(response.toLowerCase().contains("the items in your inventory are as follows"),"");
      response = sendCommandToServer("simon: INVENTORY?");
      assertTrue(response.toLowerCase().contains("the items in your inventory are as follows"),"");
      response = sendCommandToServer("simon: please show me my inv");
      assertTrue(response.toLowerCase().contains("the items in your inventory are as follows"),"");
      response = sendCommandToServer("simon: what is in my Inventory at the moment?");
      assertTrue(response.toLowerCase().contains("the items in your inventory are as follows"),"");
      response = sendCommandToServer("simon: health inventory");
      assertFalse(response.toLowerCase().contains("3"),"two valid commands worked yikes");
      response = sendCommandToServer("simon: get axe potion");
      assertFalse(response.toLowerCase().contains("you picked up"),"");
      response = sendCommandToServer("simon: get potion");
      assertTrue(response.toLowerCase().contains("you picked up"),"");
      response = sendCommandToServer("simon: drink potion chop ");
      assertTrue(response.toLowerCase().contains("you are already as healthy"),"");
      response = sendCommandToServer("simon: get axe");
      assertTrue(response.toLowerCase().contains("you picked up"),"");
      response = sendCommandToServer("simon: goto forest");
      assertTrue(response.toLowerCase().contains("you are now in"),"");
      response = sendCommandToServer("simon: chop tree elf");
      assertFalse(response.toLowerCase().contains("you cut down"),"");
      response = sendCommandToServer("simon: chop tree key");
      assertFalse(response.toLowerCase().contains("you cut down"),"");
      response = sendCommandToServer("simon: chop");
      assertFalse(response.toLowerCase().contains("you cut down"),"");
      response = sendCommandToServer("simon: chop tree cut axe drink");
      assertTrue(response.toLowerCase().contains("you cut down"),"");
      response =sendCommandToServer("simon: look ");
      assertTrue(response.toLowerCase().contains("you look"));
      response =sendCommandToServer("simon: look at the key");
      assertFalse(response.toLowerCase().contains("you look"));
  }
  @Test
  void weirdOne(){
      String response = sendCommandToServer("simon: goto forest");
      response = sendCommandToServer("simon: drink potion");
      assertFalse(response.toLowerCase().contains("you are already"),"");
      response = sendCommandToServer("simon: goto cabin");
      response = sendCommandToServer("simon: drink drink potion potion");
      assertTrue(response.toLowerCase().contains("you are already"),"");
  }
  @Test
  void testingMultiplayer(){
      String response = sendCommandToServer("simon: inv");
      assertTrue(response.toLowerCase().contains("the items in your inventory are as follows"),"");
      response = sendCommandToServer("freya: look");
      assertTrue(response.toLowerCase().contains("you also see a"),"");
      response = sendCommandToServer("freya: get axe");
      assertTrue(response.toLowerCase().contains("you picked up"),"");
      response = sendCommandToServer("simon: goto forest");
      assertTrue(response.toLowerCase().contains("you are now in"),"");
      response = sendCommandToServer("freya: goto forest");
      assertTrue(response.toLowerCase().contains("you are now in"),"");
      response = sendCommandToServer("simon: chop tree");
      assertFalse(response.toLowerCase().contains("you cut down"),"");
      response = sendCommandToServer("freya: chop tree");
      assertTrue(response.toLowerCase().contains("you cut down"),"");
      response = sendCommandToServer("simon: get log");
      assertTrue(response.toLowerCase().contains("you picked up"),"");
      response = sendCommandToServer("freya: get log");
      assertFalse(response.toLowerCase().contains("you picked up"),"");
      response = sendCommandToServer("simon: get log");
      assertFalse(response.toLowerCase().contains("you picked up"),"");
      response = sendCommandToServer("simon: goto cabin");
      assertTrue(response.toLowerCase().contains("you are now"),"");
      response = sendCommandToServer("freya: drink potion");
      assertFalse(response.toLowerCase().contains("you are already"),"");
      response = sendCommandToServer("simon: drink potion");
      assertTrue(response.toLowerCase().contains("you are already"),"");
      response = sendCommandToServer("simon: unlock with key");
      assertFalse(response.toLowerCase().contains("you unlock"),"");
      response = sendCommandToServer("freya: get key");
      assertTrue(response.toLowerCase().contains("you picked up"),"");
      response = sendCommandToServer("simon: unlock with key");
      assertFalse(response.toLowerCase().contains("you unlock"),"");
      response = sendCommandToServer("freya: goto cabin");
      assertTrue(response.toLowerCase().contains("you are now"),"");
      response = sendCommandToServer("freya: unlock with key");
      assertTrue(response.toLowerCase().contains("you unlock"),"");
      response = sendCommandToServer("simon: goto cellar");
      assertTrue(response.toLowerCase().contains("you are now in"),"");
      response = sendCommandToServer("freya: goto cellar");
      assertTrue(response.toLowerCase().contains("you are now in"),"");
  }


}