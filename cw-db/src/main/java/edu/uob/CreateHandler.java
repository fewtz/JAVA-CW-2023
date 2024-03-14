package edu.uob;

import java.util.ArrayList;
import java.util.Objects;

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
    public boolean handleCreate() {
        CurrentToken = 0;
        IncrementToken();
        switch (checkDataType()) {
            case 1:
                createDatabase();
                break;
            case 2:
                createTable();
                break;
            default:
                return false;
        }
        IncrementToken();
        if (ActiveToken.equals(";")) {
            return true;
        } else if (ActiveToken.equals("(")) {
            if(!makeTableTitles()){return false;}
            IncrementToken();
            return ActiveToken.equals(";");
        }
        return false;
    }
    private boolean makeTableTitles(){
        IncrementToken();
        if(!isAttribute()){return false;}
        IncrementToken();
        if(ActiveToken.equals(",")){return makeTableTitles();}
        return ActiveToken.equals(")");
    }
    private boolean isAttribute(){
        for(int i=0;i<ActiveToken.length();i++){
            char c = ActiveToken.charAt(i);
            if(!(Character.isAlphabetic(c)||Character.isDigit(c))){
                return false;
            }
        }
        return true;
    }
    private void createDatabase(){
        DBServer.databases.add(new Database(ActiveToken));
    }
    private void createTable(){
        DBServer.activeDatabase.tables.add(new Table(ActiveToken));
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
