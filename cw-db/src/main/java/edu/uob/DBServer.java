package edu.uob;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;

/** This class implements the DB server. */
public class DBServer {

    private static final char END_OF_TRANSMISSION = 4;
    static private String storageFolderPath;
    static public ArrayList<Database> databases;
    static private CommandHandler handler;
    static public Database activeDatabase;
    public static void main(String args[]) throws IOException {
        DBServer server = new DBServer();

        for(Database database : databases){
            for(Table table : database.tables){
                System.out.print(table.getTableAsString());
            }
        }
        if(!databases.isEmpty()) {
            activeDatabase = databases.get(0);
        }
        server.blockingListenOn(8888);
    }

    static void setupDatabases() throws IOException {
        File Databases = new File(storageFolderPath);
        File[] FilesList = Databases.listFiles();
        if(FilesList!=null){
            for(File file : FilesList) {
                if(file.isDirectory()){
                    Database newDatabase = new Database(file.getName());
                    databases.add(newDatabase);
                    newDatabase.createDatabaseFromFiles(file);
                }
            }
        }
    }
    /**
    * KEEP this signature otherwise we won't be able to mark your submission correctly.
    */
    public DBServer() {
        databases = new ArrayList<Database>();
        try{
            setupDatabases();
        }catch(Exception IOException){System.out.println("[ERROR] : Unable to set up databases");}
        storageFolderPath = Paths.get("databases").toAbsolutePath().toString();
        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(storageFolderPath));
        } catch(IOException ioe) {
            System.out.println("Can't seem to create database storage folder " + storageFolderPath);
        }
        handler = new CommandHandler();
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.DBServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming DB commands and carries out the required actions.
    */
    public String handleCommand(String command) throws IOException {
        // TODO implement your server logic here
        return handler.handleNewCommand(command);
    }

    //  === Methods below handle networking aspects of the project - you will not need to change these ! ===

    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (!Thread.interrupted()) {
                try {
                    blockingHandleConnection(s);
                } catch (IOException e) {
                    System.err.println("Server encountered a non-fatal IO error:");
                    e.printStackTrace();
                    System.err.println("Continuing...");
                }
            }
        }
    }

    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {

            System.out.println("Connection established: " + serverSocket.getInetAddress());
            while (!Thread.interrupted()) {
                String incomingCommand = reader.readLine();
                System.out.println("Received message: " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();
            }
        }
    }
}
