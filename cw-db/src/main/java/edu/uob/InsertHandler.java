package edu.uob;

import java.util.ArrayList;

public class InsertHandler extends Handler {
    String InsertValues;
    InsertHandler(ArrayList<String> Input){
        tokens = Input;
        InsertValues = "";
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
        if(!ActiveToken.equals(";")){return false;}
        activeTable.insertValues(InsertValues);
        return true;

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
}
