package edu.uob;

import java.util.ArrayList;

public class UseHandler implements Handler{
    private ArrayList<String> tokens;
    private int CurrentToken;
    private String ActiveToken;
    UseHandler(ArrayList<String> Input){
        tokens = Input;
    }
     public void IncrementToken(){
        ActiveToken = tokens.get(++CurrentToken);
    }
    public boolean handleUse(){
        CurrentToken = 0;
        IncrementToken();
        int tableIndex=isTokenDatabaseName();
        if(tableIndex>0){
            SetActiveDatabase(tableIndex);
        }else{
            throw new RuntimeException("ERROR Invalid USE command: database does not exist");
        }
        IncrementToken();
        return ActiveToken.equals(";");
    }
    private int isTokenDatabaseName(){
        for(Database database : DBServer.databases){
            if(ActiveToken.equals(database.getName())){
                return DBServer.databases.indexOf(database);
            }
        }
        return 0;
    }
    private void SetActiveDatabase(int tableIndex){
        DBServer.activeDatabase = DBServer.databases.get(tableIndex);
    }
}
