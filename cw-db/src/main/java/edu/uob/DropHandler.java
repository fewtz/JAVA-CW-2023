package edu.uob;

import java.util.ArrayList;
import java.io.*;

public class DropHandler extends Handler {
    DropHandler(ArrayList<String> Input){
        tokens = Input;
    }
    public String handleDrop(){
        CurrentToken = 0;
        IncrementToken();
        return switch (checkDataType()) {
            case 1 -> dropDatabase();
            case 2 -> dropTable();
            default -> "[ERROR] : Invalid drop type";
        };
    }
    private String dropDatabase(){
        IncrementToken();
        Database databaseToDrop=null;
        for(Database database : DBServer.databases){
            if(ActiveToken.equals(database.getName())){
                databaseToDrop = database;
            }
        }
        if(databaseToDrop==null){return "[ERROR] : Invalid database name";}
        File databaseToDeleteFile = databaseToDrop.dataBaseFile;
        File[] allContents = databaseToDeleteFile.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                System.out.println(file.getName());
                if(!file.delete()){return "[ERROR] : Unable to delete file in database";}
            }
        }
        if(!databaseToDeleteFile.delete()){return "[ERROR] : Unable to delete database";}
        DBServer.databases.remove(databaseToDrop);
        return "[OK] \n Database removed: "+ActiveToken;
    }
    private String dropTable(){
        IncrementToken();
        Table tableToDelete=null;
        for(Table table : DBServer.activeDatabase.tables){
            if((ActiveToken+".tab").equals(table.getName())){
                tableToDelete = table;
            }
        }
        if(tableToDelete == null){return "[ERROR] : Table does not exist in scope";}
        if(!tableToDelete.tableFile.delete()){return "[ERROR] : Unable to delete table";}
        DBServer.activeDatabase.tables.remove(tableToDelete);
        return "[OK] \nTable removed: "+ActiveToken;
    }
    private int checkDataType(){
        if(ActiveToken.equals("DATABASE")){
            return 1;
        }
        if(ActiveToken.equals("TABLE")){
            return 2;
        }
        return 0;
    }
}
