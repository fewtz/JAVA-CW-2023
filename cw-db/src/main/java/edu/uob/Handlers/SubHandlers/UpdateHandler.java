package edu.uob.Handlers.SubHandlers;

import edu.uob.DataStructures.DataRow;
import edu.uob.Handlers.Conditions.Comparison;
import edu.uob.Handlers.Conditions.ConditionHandler;
import edu.uob.Utilities.GenericException;

import java.util.ArrayList;

public class UpdateHandler extends ConditionHandler {
    ArrayList<Integer> attributeIndexList = new ArrayList<>();
    public UpdateHandler(ArrayList<String> Input) {
        tokens = Input;
    }
    private ArrayList<String> values = new ArrayList<>();
    public String handleUpdate() throws GenericException {
        CurrentToken = 0;
        IncrementToken();
        activeTable = isTable();
        IncrementToken();
        compareToken(ActiveToken,"SET");
        IncrementToken();
        checkNameValueList();
        compareToken(ActiveToken,"WHERE");
        IncrementToken();
        checkValidConditions(activeTable);
        pushChanges();
        return "[OK]\n Table updated successfully\n" + activeTable.getTableAsString();
    }

    private void checkNameValueList() throws GenericException {
        checkNamePair();
        IncrementToken();
        if(ActiveToken.equals(",")){checkNamePair();}
    }
    private void checkNamePair() throws GenericException {
        checkAttributeExists(ActiveToken,attributeIndexList,activeTable);
        IncrementToken();
        compareToken(ActiveToken,"=");
        IncrementToken();
        if(!isValue(new Comparison(),ActiveToken)){throw new GenericException("[ERROR] : Invalid value in name pair list");}
        if(isStringLiteral()){ActiveToken = ActiveToken.substring(1,ActiveToken.length()-1);}
        values.add(ActiveToken);
    }

    private void pushChanges() throws GenericException {
        for(DataRow dataRow : validRows){
            for(int i = 0;i<attributeIndexList.size();i++) {
                dataRow.DataPoints.set(attributeIndexList.get(i), values.get(i));
            }
        }
        activeTable.updateTableString();
        activeTable.writeToDisk();
    }

}
