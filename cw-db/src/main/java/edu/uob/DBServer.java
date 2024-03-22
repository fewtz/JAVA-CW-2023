package edu.uob;

import edu.uob.DataStructures.Database;
import edu.uob.DataStructures.Table;
import edu.uob.Handlers.CommandHandler;
import edu.uob.Utilities.GenericException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;

public class DBServer {
    private static final char END_OF_TRANSMISSION = 4;
    static private String storageFolderPath;
    static public ArrayList<Database> databases;
    static private CommandHandler handler;
    static public Database activeDatabase;
    public static void main(String args[]) throws IOException {
        DBServer server = new DBServer();
        if(!databases.isEmpty()) {
            activeDatabase = databases.get(0);
        }
        server.blockingListenOn(8888);
    }

    static void setupDatabases() throws IOException, GenericException {
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
    public DBServer() {
        storageFolderPath = Paths.get("databases").toAbsolutePath().toString();
        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(storageFolderPath));
        } catch(IOException ioe) {
            System.out.println("Can't seem to create database storage folder " + storageFolderPath);
        }
        databases = new ArrayList<Database>();
        try{
            setupDatabases();
        }catch(IOException | GenericException e){
            System.out.println("[ERROR] : Unable to set up databases");
        }
        handler = new CommandHandler();
    }

    public String handleCommand(String command){
        // TODO impliment the right case sensitivity!!!
        String returnString = "[ERROR] : Unknown error";
        try{
            returnString = handler.handleNewCommand(command);
        }catch(GenericException e){
            return e.toString();
        }
        catch(IOException e){
            return "[ERROR] : IOException";
        }
        return returnString;
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
