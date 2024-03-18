package edu.uob;

import java.util.ArrayList;
import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;

public class CreateHandler extends Handler{
    String newDatabaseName;
    String newTableName;
    Table newTable;
    CreateHandler(ArrayList<String> Input){
        tokens = Input;
    }
    public String handleCreate() throws IOException {
        CurrentToken = 0;
        IncrementToken();
        return switch (checkDataType()) {
            case 1 -> createDatabase();
            case 2 -> createTable();
            default -> "ERROR: Invalid table or database";
        };
    }
    private boolean makeTableTitles(){
        IncrementToken();
        if(!isPlainText()){return false;}
        newTable.addColumn(ActiveToken);
        IncrementToken();
        if(ActiveToken.equals(",")){return makeTableTitles();}
        return ActiveToken.equals(")");
    }
    private String createDatabase(){
        IncrementToken();
        newDatabaseName = ActiveToken;
        IncrementToken();
        if(!ActiveToken.equals(";")){return "ERROR: Missing or misplaced ';'";}
        File newDatabaseFile = new File("databases/"+newDatabaseName);
        Database newDatabase = new Database(newDatabaseName);
        if(! (newDatabaseFile.mkdirs())){return "ERROR: Unable to make new database";}
        DBServer.databases.add(newDatabase);
        newDatabase.dataBaseFile = newDatabaseFile;
        return "Created new database: "+newDatabaseName  ;
    }
    private String createTable() throws IOException {
        IncrementToken();
        newTableName = ActiveToken;
        File newTableFile = new File("databases/"+DBServer.activeDatabase.Name+"/"+ newTableName+".tab");
        if(!newTableFile.createNewFile()){return "ERROR: Unable to create new table";}
        newTable = new Table(ActiveToken,newTableFile);
        DBServer.activeDatabase.tables.add(newTable);
        IncrementToken();
        if(ActiveToken.equals(";")){return "Created new table: "+newTableName;}
        if(makeTableTitles()){return "Created new table: "+newTableName+"\n" + newTable.getTableAsString();}
        return "ERROR: Invalid table name list";
    }
    private int checkDataType(){
        System.out.println(ActiveToken);
        if(ActiveToken.equals("DATABASE")){
            return 1;
        }
        if(ActiveToken.equals("TABLE")){
            return 2;
        }
        return 0;
    }
}
