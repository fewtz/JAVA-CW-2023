package edu.uob;
import java.io.*;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;

public class Database {
    ArrayList<Table> tables;
    String Name;
    File dataBaseFile;
    Database(String inputName){
        Name = inputName;
        tables = new ArrayList<Table>();
    }
    public void createDatabaseFromFiles(File DatabaseDirectory) throws IOException {
        tables = new ArrayList<Table>();
        dataBaseFile = DatabaseDirectory;
        File[] FilesList = DatabaseDirectory.listFiles();
        if(FilesList!=null) {
            for (File file : FilesList) {
                if (file.isFile()) {
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
            Table newTable = new Table(file.getName(),file);
            tables.add(newTable);
            newTable.createTableFromFiles(buffReader,file);
        }
    }
}
