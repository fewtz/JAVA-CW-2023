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
        if((activeTable = isTable(activeTable))==null){throw new GenericException("[ERROR] : Invalid table");}
        IncrementToken();
        if(!ActiveToken.equalsIgnoreCase("SET")){throw new GenericException("[ERROR] : Expected token 'SET'");}
        IncrementToken();
        checkNameValueList();
        if(!ActiveToken.equalsIgnoreCase("WHERE")){throw new GenericException("[ERROR] : Expected token 'WHERE'");}
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
        checkAttributeExists(ActiveToken,attributeIndexList);
        IncrementToken();
        if(!ActiveToken.equals("=")){throw new GenericException("[ERROR] : Expected token '='");}
        IncrementToken();
        if(!isValue(new Comparison(),ActiveToken)){throw new GenericException("[ERROR] : Invalid value in name pair list");}
        values.add(ActiveToken);
    }

    private void pushChanges(){
        for(DataRow dataRow : validRows){
            for(int i = 0;i<attributeIndexList.size();i++) {
                dataRow.DataPoints.set(attributeIndexList.get(i), values.get(i));
            }
        }
        activeTable.updateTableString();
    }

}
