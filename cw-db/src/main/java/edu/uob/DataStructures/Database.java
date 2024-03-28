package edu.uob.DataStructures;

import edu.uob.Utilities.GenericException;

import java.io.*;
import java.util.ArrayList;

public class Database {
    public ArrayList<Table> tables;
    public String Name;
    public File dataBaseFile;
    public File maxIDFile;
    public MaxIDList idList;

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
                if (file.getName().equals("MaxIds.txt")){
                    maxIDFile = file;
                }
            }
        }
        idList = new MaxIDList(maxIDFile);
        if(FilesList!=null) {
            for (File file : FilesList) {
                if (file.isFile() && !file.getName().equals("MaxIds.txt")) {
                    readFile(file);
                }
            }
        }
    }
    public void setMaxIDFile(File file) throws IOException, GenericException {
        maxIDFile = file;
        idList = new MaxIDList(maxIDFile);
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
                throw new GenericException("[ERROR] : File not found");
            }
            Table newTable = new Table(file.getName(),file,this);
            tables.add(newTable);
            newTable.createTableFromFiles(buffReader,file);
        }
    }
}
