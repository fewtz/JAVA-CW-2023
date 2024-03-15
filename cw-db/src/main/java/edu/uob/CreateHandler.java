package edu.uob;

import java.util.ArrayList;
import java.util.Objects;

public class CreateHandler extends Handler{
    CreateHandler(ArrayList<String> Input){
        tokens = Input;
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
        if (ActiveToken.equals(";")) {return true;
        } else if (ActiveToken.equals("(")) {
            if(!makeTableTitles()){return false;}
            IncrementToken();
            return ActiveToken.equals(";");
        }
        return false;
    }
    private boolean makeTableTitles(){
        IncrementToken();
        if(!isPlainText()){return false;}
        IncrementToken();
        if(ActiveToken.equals(",")){return makeTableTitles();}
        return ActiveToken.equals(")");
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
