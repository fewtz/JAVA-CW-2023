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
    static private ArrayList<Table> tables;

    public static void main(String args[]) throws IOException {
        DBServer server = new DBServer();
        System.out.println("Time to Read from:" + storageFolderPath);
        tables = new ArrayList<Table>();
        readAllIn(tables);
        for(Table table : tables){
            System.out.print(table.getTableAsString());
        }
        server.blockingListenOn(8888);
    }

    /**
    * KEEP this signature otherwise we won't be able to mark your submission correctly.
    */
    public DBServer() {
        storageFolderPath = Paths.get("databases").toAbsolutePath().toString();
        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(storageFolderPath));
        } catch(IOException ioe) {
            System.out.println("Can't seem to create database storage folder " + storageFolderPath);
        }
    }

    static public void readAllIn(ArrayList<Table> tables) throws IOException {
        File Databases = new File(storageFolderPath);
        File[] FilesList = Databases.listFiles();
        if(FilesList!=null){
            for(File file : FilesList) {
                if(file.isFile()){
                    readFile(file,tables);
                }
            }
        }
    }

    static private void readFile(File file,ArrayList<Table> tables) throws IOException {
        if(file!=null) {
            BufferedReader buffReader;
            try {
                FileReader reader = new FileReader(file);
                buffReader = new BufferedReader(reader);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            tables.add(new Table(buffReader,file.getName()));
        }
    }







    /**
    * KEEP this signature (i.e. {@code edu.uob.DBServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming DB commands and carries out the required actions.
    */
    public String handleCommand(String command) {
        // TODO implement your server logic here
        return "";
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
