package edu.uob;

import java.util.ArrayList;

public class InsertHandler implements Handler {
    enum Modes{
        PLUSNUM,
        MINUSNUM,
        SETNUM,
        TRUE,
        FALSE,
        STRING;
    }
    private ArrayList<String> tokens;
    private int CurrentToken;
    private String ActiveToken;
    private Table activeTable;
    String InsertValue;
    Modes InsertMode;
    InsertHandler(ArrayList<String> Input){
        tokens = Input;
        InsertValue ="";
    }
    public void IncrementToken(){
        ActiveToken = tokens.get(++CurrentToken);
    }
    public boolean handleInsert() {
        CurrentToken=0;
        IncrementToken();
        if(!ActiveToken.equals("INTO")){return false;}
        IncrementToken();
        if(!isTable()){return false;}
        IncrementToken();
        if(!ActiveToken.equals("VALUES")){return false;}
        IncrementToken();
        if(!ActiveToken.equals("(")){return false;}
        IncrementToken();
        if(!isValueList()){return false;}
        IncrementToken();
        if(!ActiveToken.equals(")")){return false;}
        IncrementToken();
        return ActiveToken.equals(";");

    }
    private boolean isValueList(){
        if (!isValue()){return false;}
        IncrementToken();
        if(ActiveToken.equals(",")){
            return isValueList();
        }
        return true;
    }
    private boolean isValue(){
        return ActiveToken.equals("NULL")||isIntegerLiteral()||isFloatLiteral()||isBooleanLiteral()||isStringLiteral();
    }
    private boolean notValFirstValue(String buffer) {
        char firstValue =ActiveToken.charAt(0);
        if(firstValue=='+'){
            InsertMode = Modes.PLUSNUM;
            return false;
        }
        else if (firstValue=='-') {
            InsertMode = Modes.MINUSNUM;
            return false;
        }
        else if (Character.isDigit(firstValue)) {
            InsertMode = Modes.SETNUM;
            buffer+=firstValue;
            return false;
        }
        else{
            return true;
        }
    }
    private boolean isIntegerLiteral(){
        String bufferValue="";
        if (notValFirstValue(bufferValue)) return false;
        for(int i=1;i<ActiveToken.length();i++){
            char character = ActiveToken.charAt(i);
            if(!(Character.isDigit(character))){return false;}
            bufferValue+=character;
        }
        InsertValue=bufferValue;
        return true;
    }

    private boolean isBooleanLiteral(){
        if(!(ActiveToken.equals("TRUE")||ActiveToken.equals("FALSE"))){return false;}
        InsertValue=ActiveToken;
        return true;
    }
    private boolean isFloatLiteral(){
        String bufferValue="";
        if (notValFirstValue(bufferValue)) return false;
        boolean passedPeriod=false;
        for(int i=1;i<ActiveToken.length();i++){
            char character = ActiveToken.charAt(i);
            if(!(Character.isDigit(character)||character=='.')){return false;}
            if(character=='.') {if(!passedPeriod) {passedPeriod = true;} else {return false;}
            }
            bufferValue+=character;
        }
        InsertValue=bufferValue;
        return true;
    }
    private boolean isStringLiteral(){
        if(ActiveToken.charAt(0)!='\''){return false;}
        InsertMode = Modes.STRING;
        String bufferValue="";
        for(int i=1;i<ActiveToken.length()-1;i++) {
            char character = ActiveToken.charAt(i);
            if(character==34||character==39||character==124){return false;}
            bufferValue+=character;
        }
        if(ActiveToken.charAt(0)!='\''){return false;}
        InsertValue=bufferValue;
        return true;
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
