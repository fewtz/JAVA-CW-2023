package edu.uob.Handlers.SubHandlers;

import edu.uob.DBServer;
import edu.uob.DataStructures.DataRow;
import edu.uob.Handlers.Conditions.Comparison;
import edu.uob.Handlers.Conditions.ConditionHandler;
import edu.uob.Utilities.GenericException;
import java.util.ArrayList;

public class DeleteHandler extends ConditionHandler{
    public DeleteHandler(ArrayList<String> Input) {
        tokens = Input;
    }

    public String handleDelete() throws GenericException {
        if(DBServer.activeDatabase == null){throw new GenericException("[ERROR] : Active database not set");}
        CurrentToken = 0;
        IncrementToken();
        compareToken(ActiveToken,"FROM");
        IncrementToken();
        activeTable = isTable();
        IncrementToken();
        compareToken(ActiveToken,"WHERE");
        IncrementToken();
        checkValidConditions(activeTable);
        pushChanges();
        return "[OK]\nRows successfully deleted\n"+activeTable.getTableAsString();
    }

    private void pushChanges() throws GenericException {
        for(DataRow dataRow : validRows){
            activeTable.removeRow(dataRow);
        }
        activeTable.updateTableString();
        activeTable.writeToDisk();
    }
}
