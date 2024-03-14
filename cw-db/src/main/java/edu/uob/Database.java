package edu.uob;
import java.io.*;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;

public class Database {
    ArrayList<Table> tables;
    String Name;
    Database(String inputName){
        Name = inputName;
    }
    public void createDatabaseFromFiles(File Input) throws IOException {
        File[] FilesList = Input.listFiles();
        if(FilesList[0]!=null) {
            for (File file : FilesList) {
                if (file.isDirectory()) {
                    readFile(file);
                }
            }
        }
    }
    public String getName(){
        return Name;
    }
    private void readFile(File file) throws IOException {
        if(file!=null) {
            BufferedReader buffReader;
            try {
                FileReader reader = new FileReader(file);
                buffReader = new BufferedReader(reader);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Table newTable = new Table(file.getName());
            tables.add(newTable);
            newTable.createTableFromFiles(buffReader);
        }
    }
}
