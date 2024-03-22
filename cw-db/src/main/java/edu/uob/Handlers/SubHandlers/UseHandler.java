package edu.uob.Handlers.SubHandlers;

import edu.uob.DBServer;
import edu.uob.DataStructures.Database;
import edu.uob.Handlers.Handler;
import edu.uob.Utilities.GenericException;

import java.util.ArrayList;

public class UseHandler extends Handler {
    private String ActiveDatabaseName;
    private int indexOfDatabase;
    public UseHandler(ArrayList<String> Input){
        tokens = Input;
    }

    public String handleUse() throws GenericException {
        CurrentToken = 0;
        IncrementToken();
        testTokenDatabaseName();
        IncrementToken();
        if(!ActiveToken.equals(";")){throw new GenericException("[ERROR] : Missing or misplaced ';'") ;}
        DBServer.activeDatabase = DBServer.databases.get(indexOfDatabase);
        return "[OK] \nActive Database: " + ActiveDatabaseName;
    }
    private void testTokenDatabaseName() throws GenericException {
        for(Database database : DBServer.databases){
            if(ActiveToken.equals(database.getName())){
                indexOfDatabase =  DBServer.databases.indexOf(database);
                ActiveDatabaseName = ActiveToken;
                return;
            }
        }
        throw new GenericException("[ERROR] : Invalid database name");
    }
    private void SetActiveDatabase(int tableIndex){
        DBServer.activeDatabase = DBServer.databases.get(tableIndex);
    }
}
