package edu.uob.Handlers.SubHandlers;
import edu.uob.DataStructures.DataRow;
import edu.uob.DataStructures.Table;
import edu.uob.Handlers.Conditions.Comparison;
import edu.uob.Handlers.Conditions.ConditionHandler;
import edu.uob.Utilities.GenericException;
import java.util.ArrayList;
import edu.uob.Utilities.DeepObjectCopier;
import java.io.IOException;
import java.io.*;
public class JoinHandler extends ConditionHandler{
    private Table joiningTable;
    private Table resultTable;
    private ArrayList<Integer> joiningAttributes = new ArrayList<>();
    public JoinHandler(ArrayList<String> Input) {
        tokens = Input;
    }

    public String handleJoin() throws GenericException {
        CurrentToken = 0;
        IncrementToken();
        activeTable = isTable();
        IncrementToken();
        compareToken(ActiveToken,"AND");
        IncrementToken();
        joiningTable = isTable();
        IncrementToken();
        compareToken(ActiveToken,"ON");
        IncrementToken();
        checkAttributeExists(ActiveToken,joiningAttributes,activeTable);
        IncrementToken();
        compareToken(ActiveToken,"AND");
        IncrementToken();
        checkAttributeExists(ActiveToken,joiningAttributes,joiningTable);
        joinTables();
        return "[OK]\n Tables Joined\n"+resultTable.getTableAsString();
    }

    private void joinTables() throws GenericException {
        resultTable = (Table) DeepObjectCopier.copy(activeTable);
        resultTable.joinTable(joiningTable,joiningAttributes.get(0),joiningAttributes.get(1));
    }
}
