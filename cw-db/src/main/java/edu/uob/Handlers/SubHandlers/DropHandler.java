package edu.uob.Handlers.SubHandlers;

import edu.uob.DBServer;
import edu.uob.DataStructures.Database;
import edu.uob.Handlers.Handler;
import edu.uob.Utilities.GenericException;
import edu.uob.DataStructures.Table;

import java.util.ArrayList;
import java.io.*;

public class DropHandler extends Handler {
    public DropHandler(ArrayList<String> Input){
        tokens = Input;
    }
    public String handleDrop() throws GenericException, IOException {

        CurrentToken = 0;
        IncrementToken();
        return switch (checkDataType()) {
            case 1 -> dropDatabase();
            case 2 -> dropTable();
            default -> throw new GenericException( "[ERROR] : Invalid drop type");
        };
    }

    //TODO clean this up
    private String dropDatabase() throws GenericException {
        IncrementToken();
        Database databaseToDrop=null;
        for(Database database : DBServer.databases){
            if(ActiveToken.equals(database.getName())){
                databaseToDrop = database;
            }
        }
        if(databaseToDrop==null){throw new GenericException("[ERROR] : Database to delete does not exist in scope") ;}
        File databaseToDeleteFile = databaseToDrop.dataBaseFile;
        File[] allContents = databaseToDeleteFile.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                if(!file.delete()){throw new GenericException("[ERROR] : File inside database could not be deleted");}
            }
        }
        if(!databaseToDeleteFile.delete()){throw new GenericException("[ERROR] : Database could not be deleted");}
        DBServer.databases.remove(databaseToDrop);
        return "[OK] \n Database removed: "+ActiveToken;
    }
    private String dropTable() throws GenericException,IOException {
        if(DBServer.activeDatabase == null){throw new GenericException("[ERROR] : Active database not set");}
        IncrementToken();
        Table tableToDelete=null;
        for(Table table : DBServer.activeDatabase.tables){
            if((ActiveToken).equals(table.getName())){
                tableToDelete = table;
            }
        }
        if(tableToDelete == null){throw new GenericException("[ERROR] : Table to delete does not exist in scope") ;}
        if(!tableToDelete.tableFile.delete()){throw new GenericException("[ERROR] : Table could not be deleted");}
        DBServer.activeDatabase.tables.remove(tableToDelete);
        return "[OK] \nTable removed: "+ActiveToken;
    }
    private int checkDataType(){
        if(ActiveToken.equalsIgnoreCase("DATABASE")){
            return 1;
        }
        if(ActiveToken.equalsIgnoreCase("TABLE")){
            return 2;
        }
        return 0;
    }
}
