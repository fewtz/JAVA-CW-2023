package edu.uob;

import java.util.ArrayList;

public class CreateHandler implements Handler{
    private ArrayList<String> tokens;
    private int CurrentToken;
    private String ActiveToken;
    CreateHandler(ArrayList<String> Input){
        tokens = Input;
    }
    public void IncrementToken(){
        ActiveToken = tokens.get(++CurrentToken);
    }
    public boolean handleCreate(){
        CurrentToken = 0;
        IncrementToken();
        switch(checkDataType()){
            case 1:
                createDatabase();
                break;
            case 2:
                createTable();
                break;
            default:
                return false;
        }
        return false;
    }
    private boolean createDatabase(){
        DBServer.databases.add(new Database(ActiveToken));
        return false;
    }
    private boolean createTable(){
        DBServer.activeDatabase.tables.add(new Table(ActiveToken));
        return false;
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
