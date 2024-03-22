package edu.uob.Handlers.SubHandlers;

import edu.uob.Handlers.Conditions.ConditionHandler;
import edu.uob.Handlers.Handler;
import edu.uob.Utilities.GenericException;

import java.util.ArrayList;

public class AlterHandler extends Handler {
    private int AlterationType;
    public AlterHandler(ArrayList<String> Input){
        tokens = Input;
    }
    public String handleAlter() throws GenericException {
        CurrentToken=0;
        IncrementToken();
        if(!ActiveToken.equalsIgnoreCase("TABLE")){throw new GenericException("[ERROR] : Only table alters are permitted") ;}
        IncrementToken();
        if((activeTable = isTable(activeTable))==null){throw new GenericException("[ERROR] : Invalid table");}
        IncrementToken();
        AlterationType=whatAlteration();
        IncrementToken();
        alterAttribute();
        IncrementToken();
        if(!ActiveToken.equals(";")){return "[ERROR] : Missing or misplaced ';'";}
        return "[OK] \nTable Altered successfully \n" + activeTable.getTableAsString();
    }
    private int whatAlteration() throws GenericException {
        return switch (ActiveToken.toUpperCase()) {
            case "ADD" -> 1;
            case "DROP" -> 2;
            default -> throw new GenericException("[ERROR] : Invalid alteration type");
        };
    }
    private void alterAttribute() throws GenericException {
        testNotKeyword();
        switch (AlterationType) {
            case 1 -> {
                for (String title : activeTable.columnNames) {
                    if (ActiveToken.equals(title)) {
                        throw new GenericException("[ERROR] : Column already exists");
                    }
                }
                activeTable.addColumn(ActiveToken);
            }
            case 2 -> {
                for (String title : activeTable.columnNames) {
                    if (ActiveToken.equals(title)) {
                        activeTable.removeColumn(title);
                        return;
                    }
                }
                throw new GenericException("[ERROR] : Column does not exist");
            }
        }
    }

    public static class UpdateHandler extends ConditionHandler {
    }
}
