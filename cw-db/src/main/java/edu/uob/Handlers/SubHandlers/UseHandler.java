package edu.uob;

import java.util.ArrayList;

public class UseHandler extends Handler{
    private String ActiveDatabaseName;
    private int indexOfDatabase;
    UseHandler(ArrayList<String> Input){
        tokens = Input;
    }

    public String handleUse(){
        CurrentToken = 0;
        IncrementToken();
        if(!isTokenDatabaseName()){return "[ERROR] : Invalid database name";}
        IncrementToken();
        if(!ActiveToken.equals(";")){return "[ERROR] : Missing or misplaced ';'";}
        DBServer.activeDatabase = DBServer.databases.get(indexOfDatabase);
        return "[OK] \nActive Database: " + ActiveDatabaseName;
    }
    private boolean isTokenDatabaseName(){
        for(Database database : DBServer.databases){
            if(ActiveToken.equals(database.getName())){
                indexOfDatabase =  DBServer.databases.indexOf(database);
                ActiveDatabaseName = ActiveToken;
                return true;
            }
        }
        return false;
    }
    private void SetActiveDatabase(int tableIndex){
        DBServer.activeDatabase = DBServer.databases.get(tableIndex);
    }
}
