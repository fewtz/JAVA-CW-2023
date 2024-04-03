package edu.uob.Handlers;

import edu.uob.DBServer;
import edu.uob.Utilities.GenericException;
import edu.uob.DataStructures.Table;
import java.util.ArrayList;

public abstract class Handler {
    public ArrayList<String> tokens;
    public int CurrentToken;
    public String ActiveToken;
    public Table activeTable;
    String InsertValues;

    public void testNotKeyword() throws GenericException {
        if(ActiveToken.equalsIgnoreCase("SELECT") ||
                ActiveToken.equalsIgnoreCase("DATABASE") || ActiveToken.equalsIgnoreCase("TABLE") ||ActiveToken.equalsIgnoreCase("TRUE") ||
                ActiveToken.equalsIgnoreCase("FALSE") ||ActiveToken.equalsIgnoreCase("ADD") ||ActiveToken.equalsIgnoreCase("DROP") ||
                ActiveToken.equalsIgnoreCase("CREATE") ||ActiveToken.equalsIgnoreCase("INSERT") ||ActiveToken.equalsIgnoreCase("UPDATE") ||
                ActiveToken.equalsIgnoreCase("SET") ||ActiveToken.equalsIgnoreCase("WHERE") ||ActiveToken.equalsIgnoreCase("DELETE") ||
                ActiveToken.equalsIgnoreCase("FROM") ||ActiveToken.equalsIgnoreCase("JOIN") ||ActiveToken.equalsIgnoreCase("AND") ||
                ActiveToken.equalsIgnoreCase("OR") ||ActiveToken.equalsIgnoreCase("ON") ||ActiveToken.equalsIgnoreCase("LIKE") ||
                ActiveToken.equalsIgnoreCase("USE") ||ActiveToken.equalsIgnoreCase("ALTER") ||ActiveToken.equalsIgnoreCase("INTO") ||
                ActiveToken.equalsIgnoreCase("VALUES")){
            throw new GenericException("[ERROR] : Cannot use keyword as table,database or attribute title");
        }
    }
    public void IncrementToken() throws GenericException {
        try {
            ActiveToken = tokens.get(++CurrentToken);
        }catch(Exception e){
            throw new GenericException("[ERROR]: Ran out of tokens early");
        }
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
        if(!(ActiveToken.equalsIgnoreCase("TRUE")||ActiveToken.equalsIgnoreCase("FALSE"))){return false;}
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
    public Table isTable() throws GenericException {
        for(Table table : DBServer.activeDatabase.tables){
            if((ActiveToken).equals(table.getName())){
                if(table==null){throw new GenericException("[ERROR] : Attempted to access null table");}
                return table;
            }
        }
        throw new GenericException("[ERROR] : Not a valid table");
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
    public void checkAttributeExists(String attribute,ArrayList<Integer> attributeIndexList,Table table) throws GenericException {
        boolean foundValue = false;
        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            String title = table.columnNames.get(i);
            if (attribute.equals(title)) {
                attributeIndexList.add(i);
                foundValue = true;
            }
        }
        if (!foundValue) {
            throw new GenericException("[ERROR] : That attribute is not in scope");
        }
    }
    public void compareToken(String token, String target) throws GenericException {
        if(!token.equalsIgnoreCase(target)){throw new GenericException("[ERROR] : Expected token :"+target);}
    }
}
