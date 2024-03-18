package edu.uob;

import java.util.ArrayList;

abstract class Handler {
    enum Modes{
        PLUSNUM,
        MINUSNUM,
        SETNUM,
        TRUE,
        FALSE,
        STRING;
    }
    public ArrayList<String> tokens;
    public int CurrentToken;
    public String ActiveToken;
    public Table activeTable;
    String InsertValues;
    public void IncrementToken(){
        ActiveToken = tokens.get(++CurrentToken);
    }
    public boolean isPlainText(){
        for(int i=0;i<ActiveToken.length();i++){
            char c = ActiveToken.charAt(i);
            if(!(Character.isAlphabetic(c)||Character.isDigit(c))){
                return false;
            }
        }
        return true;
    }
    public boolean isIntegerLiteral(){
        String bufferValue="";
        if (notValFirstValue(bufferValue)) return false;
        for(int i=1;i<ActiveToken.length();i++){
            char character = ActiveToken.charAt(i);
            if(!(Character.isDigit(character))){return false;}
            bufferValue+=character;
        }
        InsertValues+=bufferValue + "\t";
        return true;
    }

    public boolean isBooleanLiteral(){
        if(!(ActiveToken.equals("TRUE")||ActiveToken.equals("FALSE"))){return false;}
        InsertValues+=ActiveToken + "\t";
        return true;
    }
    public boolean isFloatLiteral(){
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
        InsertValues+=bufferValue + "\t";
        return true;
    }
    public boolean isStringLiteral(){
        if(ActiveToken.charAt(0)!='\''){return false;}
        String bufferValue="";
        for(int i=1;i<ActiveToken.length()-1;i++) {
            char character = ActiveToken.charAt(i);
            if(character==34||character==39||character==124){return false;}
            bufferValue+=character;
        }
        if(ActiveToken.charAt(0)!='\''){return false;}
        InsertValues+=bufferValue + "\t";
        return true;
    }
    public Table isTable(Table inpActiveTable){
        for(Table table : DBServer.activeDatabase.tables){
            if((ActiveToken+".tab").equals(table.getName())){
                System.out.println(table.getName());
                return table;
            }
        }
        return null;
    }
    private boolean notValFirstValue(String buffer) {
        char firstValue =ActiveToken.charAt(0);
        if(firstValue=='+'){
            return false;
        }
        else if (firstValue=='-') {
            return false;
        }
        else if (Character.isDigit(firstValue)) {
            buffer+=firstValue;
            return false;
        }
        else{
            return true;
        }
    }
}
