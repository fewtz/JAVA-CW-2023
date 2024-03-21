package edu.uob;

import java.util.ArrayList;

public class InsertHandler extends Handler {

    //NEEDS MORE TESTING FREYA!

    String InsertValues;
    InsertHandler(ArrayList<String> Input){
        tokens = Input;
        InsertValues = "";
    }
    public String handleInsert() throws GenericException {
        CurrentToken=0;
        IncrementToken();
        if(!ActiveToken.equals("INTO")){return "[ERROR] : Expected token 'INTO'";}
        IncrementToken();
        if((activeTable = isTable(activeTable))==null){return "[ERROR] : Invalid table";}
        IncrementToken();
        if(!ActiveToken.equals("VALUES")){return "[ERROR] : Expected token 'VALUES'";}
        IncrementToken();
        if(!ActiveToken.equals("(")){return "[ERROR] : Expected token '('";}
        if(!isValueList()){return "[ERROR] : Invalid value list";}
        if(!ActiveToken.equals(")")){return "[ERROR] : Expected token ')'";}
        IncrementToken();
        if(!ActiveToken.equals(";")){return "[ERROR] : Missing or misplaced ';'";}
        if(!activeTable.insertValues(InsertValues)){return "[ERROR] : Invalid input values";}
        if(!activeTable.writeToDisk()){return "[ERROR] : Unable to write to file";}
        return "[OK] \nValues insert successfully \n"+activeTable.getTableAsString();
    }
    private boolean isValueList() throws GenericException {
        IncrementToken();
        if (!isValue()){return false;}
        if(isStringLiteral()){
            ActiveToken = ActiveToken.substring(1,ActiveToken.length()-1);
        }
        InsertValues += ActiveToken + "\t";
        IncrementToken();
        if(ActiveToken.equals(",")){
            return isValueList();
        }
        return true;
    }
    private boolean isValue(){
        return ActiveToken.equals("NULL")||isIntegerLiteral()||isFloatLiteral()||isBooleanLiteral()||isStringLiteral();
    }
}
