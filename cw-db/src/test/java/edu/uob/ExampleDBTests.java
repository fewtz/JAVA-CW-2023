package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

public class ExampleDBTests {

    private DBServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    public void setup() {
        server = new DBServer();
    }

    // Random name generator - useful for testing "bare earth" queries (i.e. where tables don't previously exist)
    private String generateRandomName() {
        String randomName = "";
        for(int i=0; i<10 ;i++) randomName += (char)( 97 + (Math.random() * 25.0));
        return randomName;
    }

    private String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
        "Server took too long to respond (probably stuck in an infinite loop)");
    }

    // A basic test that creates a database, creates a table, inserts some test data, then queries it.
    // It then checks the response to see that a couple of the entries in the table are returned as expected
    @Test
    public void testBasicCreateAndQuery() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Sion', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Rob', 35, FALSE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Chris', 20, FALSE);");
        String response = sendCommandToServer("SELECT * FROM marks;");
        assertTrue(response.contains("[OK]"), "A valid query was made, however an [OK] tag was not returned");
        assertFalse(response.contains("[ERROR]"), "A valid query was made, however an [ERROR] tag was returned");
        assertTrue(response.contains("Simon"), "An attempt was made to add Simon to the table, but they were not returned by SELECT *");
        assertTrue(response.contains("Chris"), "An attempt was made to add Chris to the table, but they were not returned by SELECT *");
    }

    // A test to make sure that querying returns a valid ID (this test also implicitly checks the "==" condition)
    // (these IDs are used to create relations between tables, so it is essential that suitable IDs are being generated and returned !)
    @Test
    public void testQueryID() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        String response = sendCommandToServer("SELECT id FROM marks WHERE name == 'Simon';");
        // Convert multi-lined responses into just a single line
        String singleLine = response.replace("\n"," ").trim();
        // Split the line on the space character
        String[] tokens = singleLine.split(" ");
        // Check that the very last token is a number (which should be the ID of the entry)
        String lastToken = tokens[tokens.length-1];
        try {
            Integer.parseInt(lastToken);
        } catch (NumberFormatException nfe) {
            fail("The last token returned by `SELECT id FROM marks WHERE name == 'Simon';` should have been an integer ID, but was " + lastToken);
        }
    }

    // A test to make sure that databases can be reopened after server restart
    @Test
    public void testTablePersistsAfterRestart() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        // Create a new server object
        server = new DBServer();
        sendCommandToServer("USE " + randomName + ";");
        String response = sendCommandToServer("SELECT * FROM marks;");
        assertTrue(response.contains("Simon"), "Simon was added to a table and the server restarted - but Simon was not returned by SELECT *");
    }

    // Test to make sure that the [ERROR] tag is returned in the case of an error (and NOT the [OK] tag)
    @Test
    public void testForErrorTag() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        String response = sendCommandToServer("SELECT * FROM libraryfines;");
        assertTrue(response.contains("[ERROR]"), "An attempt was made to access a non-existent table, however an [ERROR] tag was not returned");
        assertFalse(response.contains("[OK]"), "An attempt was made to access a non-existent table, however an [OK] tag was returned");
    }
    @Test
    public void Use(){
        String randomName = generateRandomName();
        String response = sendCommandToServer("USE " + randomName + ";");
        assertTrue(response.contains("[ERROR]"), "An attempt was made to use an invalid database, however an [ERROR] tag was not returned");
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        response = sendCommandToServer("USE " + randomName + ";");
        assertFalse(response.contains("[ERROR]"), "An attempt was made to use a valid database, however an [ERROR] tag was returned");
    }
    @Test
    public void Create(){
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        String response = sendCommandToServer("CREATE DATABASE " + randomName + ";");
        assertTrue(response.contains("[ERROR]"), "An attempt was made to create an already created database, however an [ERROR] tag was not returned");
        sendCommandToServer("USE " + randomName + ";");
        response = sendCommandToServer("CREATE TABLE " + randomName + " (A,B,C);");
        assertTrue(response.contains("[OK]"), "An attempt was made to create valid table, and it failed.");
        response = sendCommandToServer("CREATE TABLE " + randomName + " (A,B,C);");
        assertTrue(response.contains("[ERROR]"), "An attempt was made to create an already created table, however an [ERROR] tag was not returned");
    }
    @Test
    public void Alter(){
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        randomName = generateRandomName();
        sendCommandToServer("CREATE TABLE " + randomName + " (A,B,C);");
        String response = sendCommandToServer("ALTER TABLE " + randomName + " ADD D;");
        assertTrue(response.contains("[OK]"), "An attempt was made to add a valid column but it failed.");
        response = sendCommandToServer("ALTER TABLE " + randomName + " ADD D;");
        assertFalse(response.contains("[OK]"), "An attempt was made to add a column that already existed, and it didnt fail.");
        response= sendCommandToServer("ALTER TABLE " + randomName + " DROP D;");
        assertTrue(response.contains("[OK]"), "An attempt was made to drop a valid column but it failed.");
        response = sendCommandToServer("ALTER TABLE " + randomName + " DROP D;");
        assertFalse(response.contains("[OK]"), "An attempt was made to drop a nonexistant column, and it didnt fail.");
        sendCommandToServer("ALTER TABLE " + randomName + " ADD D;");
        sendCommandToServer("INSERT INTO "+randomName+" VALUES (1,2,3)");
        response= sendCommandToServer("ALTER TABLE " + randomName + " DROP D;");
        assertTrue(response.contains("[OK]"), "An attempt was made to drop a non empty column, and it failed.");
        response = sendCommandToServer("ALTER TABLE blahblahblah ADD D;");
        assertFalse(response.contains("[OK]"), "An attempt was made to add a  column to a nonexistant table, and it didnt fail.");
        response = sendCommandToServer("ALTER TABLE blahblahblah DROP D;");
        assertFalse(response.contains("[OK]"), "An attempt was made to drop a nonexistant column from a nonexistant table, and it didnt fail.");
        response = sendCommandToServer("ALTER TABLE blahblahblah DROP D;");
        assertFalse(response.contains("[OK]"), "An attempt was made to drop a nonexistant column from a nonexistant table, and it didnt fail.");
    }

}
