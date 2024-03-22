package edu.uob.Handlers.SubHandlers;

import edu.uob.Handlers.Handler;
import edu.uob.Utilities.GenericException;

import java.util.ArrayList;

public class InsertHandler extends Handler {

    String InsertValues;
    public InsertHandler(ArrayList<String> Input){
        tokens = Input;
        InsertValues = "";
    }
    public String handleInsert() throws GenericException {
        CurrentToken=0;
        IncrementToken();
        if(!ActiveToken.equalsIgnoreCase("INTO")){throw new GenericException( "[ERROR] : Expected token 'INTO'");}
        IncrementToken();
        if((activeTable = isTable(activeTable))==null){throw new GenericException("[ERROR] : Invalid table") ;}
        IncrementToken();
        if(!ActiveToken.equalsIgnoreCase("VALUES")){throw new GenericException( "[ERROR] : Expected token 'VALUES'");}
        IncrementToken();
        if(!ActiveToken.equals("(")){throw new GenericException( "[ERROR] : Expected token '('");}
        isValueList();
        if(!ActiveToken.equals(")")){throw new GenericException( "[ERROR] : Expected token ')'");}
        IncrementToken();
        if(!ActiveToken.equals(";")){throw new GenericException( "[ERROR] : Expected token ';'");}
        activeTable.insertValues(InsertValues);
        activeTable.writeToDisk();
        return "[OK] \nValues insert successfully \n"+activeTable.getTableAsString();
    }
    private void isValueList() throws GenericException {
        IncrementToken();
        checkIsValue();
        if(isStringLiteral()){
            ActiveToken = ActiveToken.substring(1,ActiveToken.length()-1);
        }
        InsertValues += ActiveToken + "\t";
        IncrementToken();
        if(ActiveToken.equals(",")){
            isValueList();
        }
    }
    private void checkIsValue() throws GenericException{
        if(!(ActiveToken.equalsIgnoreCase("NULL")||isIntegerLiteral()||isFloatLiteral()||isBooleanLiteral()||isStringLiteral())){
            throw new GenericException("[ERROR] : Invalid datatype");
        }
    }
}
