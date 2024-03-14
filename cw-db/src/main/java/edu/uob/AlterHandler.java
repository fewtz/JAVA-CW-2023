package edu.uob;

import java.util.ArrayList;

public class AlterHandler implements Handler{
    private ArrayList<String> tokens;
    private int CurrentToken;
    private String ActiveToken;
    private Table activeTable;
    private int AlterationType;
    AlterHandler(ArrayList<String> Input){
        tokens = Input;
    }
    public void IncrementToken(){
        ActiveToken = tokens.get(++CurrentToken);
    }
    public boolean handleAlter(){
        CurrentToken=0;
        IncrementToken();
        if(!ActiveToken.equals("TABLE")){return false;}
        IncrementToken();
        if(!isTable()){return false;}
        IncrementToken();
        if((AlterationType=whatAlteration())==0){return false;}
        IncrementToken();
        if(!alterAttribute()){return false;}
        IncrementToken();
        return ActiveToken.equals(";");
    }
    private boolean alterAttribute(){
        switch(AlterationType) {
            case 1:
                for (String title : activeTable.columnNames) {
                    if (ActiveToken.equals(title)) {
                        return false;
                    }
                }
                activeTable.addCollumn(ActiveToken);
                return true;
            case 2:
                for (String title : activeTable.columnNames) {
                    if (ActiveToken.equals(title)) {
                        activeTable.removeColumn(title);
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }
    private int whatAlteration(){
        return switch (ActiveToken) {
            case "ADD" -> 1;
            case "DROP" -> 2;
            default -> 0;
        };
    }
    private boolean isTable(){
        for(Table table : DBServer.activeDatabase.tables){
            if(ActiveToken.equals(table.getName())){
                activeTable = table;
                return true;
            }
        }
        return false;
    }
}
