package edu.uob.Handlers.SubHandlers;

import edu.uob.DBServer;
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
        if(DBServer.activeDatabase == null){throw new GenericException("[ERROR] : Active database not set");}
        CurrentToken=0;
        IncrementToken();
        compareToken(ActiveToken,"INTO");
        IncrementToken();
        activeTable = isTable();
        IncrementToken();
        compareToken(ActiveToken,"VALUES");
        IncrementToken();
        compareToken(ActiveToken,"(");
        isValueList();
        compareToken(ActiveToken,")");
        IncrementToken();
        compareToken(ActiveToken,";");
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
