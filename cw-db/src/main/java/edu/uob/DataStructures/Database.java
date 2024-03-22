package edu.uob.DataStructures;

import edu.uob.Utilities.GenericException;

import java.io.*;
import java.util.ArrayList;

public class Database {
    public ArrayList<Table> tables;
    public String Name;
    public File dataBaseFile;
    public Database(String inputName){
        Name = inputName;
        tables = new ArrayList<Table>();
    }
    public void createDatabaseFromFiles(File DatabaseDirectory) throws IOException, GenericException {
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
    private void readFile(File file) throws IOException, GenericException {
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
