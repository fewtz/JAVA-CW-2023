package edu.uob;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class DropHandler implements Handler {
    private ArrayList<String> tokens;
    private int CurrentToken;
    private String ActiveToken;
    DropHandler(ArrayList<String> Input){
        tokens = Input;
    }
    public void IncrementToken(){
        ActiveToken = tokens.get(++CurrentToken);
    }

    public boolean handleDrop(){
        CurrentToken = 0;
        IncrementToken();
        switch (checkDataType()) {
            case 1:
                if(!dropDatabase()){return false;}
                break;
            case 2:
                if(!dropTable()){return false;}
                break;
            default:
                return false;
        }
        IncrementToken();
        return ActiveToken.equals(";");
    }
    private boolean dropDatabase(){
        IncrementToken();
        return DBServer.databases.removeIf(database -> ActiveToken.equals(database.getName()));
    }
    private boolean dropTable(){
        IncrementToken();
        return DBServer.activeDatabase.tables.removeIf(table -> ActiveToken.equals(table.getName()));
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
