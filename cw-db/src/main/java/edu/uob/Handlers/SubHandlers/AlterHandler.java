package edu.uob.Handlers.SubHandlers;

import edu.uob.DBServer;
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
        if(DBServer.activeDatabase == null){throw new GenericException("[ERROR] : Active database not set");}
        CurrentToken=0;
        IncrementToken();
        compareToken(ActiveToken,"TABLE");
        IncrementToken();
        activeTable = isTable();
        IncrementToken();
        AlterationType=whatAlteration();
        IncrementToken();
        alterAttribute();
        IncrementToken();
        compareToken(ActiveToken,";");
        activeTable.updateTableString();
        activeTable.writeToDisk();
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
        if(ActiveToken.equals("id")){throw new GenericException("[ERROR] : Cannot add or remove id column");}
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
}
