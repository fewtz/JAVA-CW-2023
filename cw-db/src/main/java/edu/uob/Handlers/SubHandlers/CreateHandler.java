package edu.uob.Handlers.SubHandlers;

import edu.uob.DBServer;
import edu.uob.DataStructures.Database;
import edu.uob.Handlers.Handler;
import edu.uob.Utilities.GenericException;
import edu.uob.DataStructures.Table;

import java.util.ArrayList;
import java.io.*;

public class CreateHandler extends Handler {
    String newDatabaseName;
    String newTableName;
    Table newTable;
    public CreateHandler(ArrayList<String> Input){
        tokens = Input;
    }
    public String handleCreate() throws IOException, GenericException {
        CurrentToken = 0;
        IncrementToken();
        return switch (checkDataType()) {
            case 1 -> createDatabase();
            case 2 -> createTable();
            default -> throw new GenericException("[ERROR] : Expected either DATABASE or TABLE");
        };
    }
    private int checkDataType(){
        System.out.println(ActiveToken);
        if(ActiveToken.equalsIgnoreCase("DATABASE")){return 1;}
        if(ActiveToken.equalsIgnoreCase("TABLE")){return 2;}
        return 0;
    }
        //TODO clean this up
    private String createDatabase() throws GenericException, IOException {
        IncrementToken();
        newDatabaseName = ActiveToken;
        testNotKeyword();
        IncrementToken();
        compareToken(ActiveToken,";");
        File newDatabaseFile = new File("databases/"+newDatabaseName);
        Database newDatabase = new Database(newDatabaseName);
        if(! (newDatabaseFile.mkdirs())){throw new GenericException( "[ERROR] : Unable to make new database");}
        DBServer.databases.add(newDatabase);
        newDatabase.dataBaseFile = newDatabaseFile;
        createMaxIdFile(newDatabase);
        return "[OK] \nCreated new database: "+newDatabaseName  ;
    }
    private String createTable() throws IOException, GenericException {
        if(DBServer.activeDatabase == null){throw new GenericException("[ERROR] : Active database not set");}
        IncrementToken();
        makeTable();
        IncrementToken();
        if(ActiveToken.equals(";")){return "[OK] \nCreated new table: "+newTableName;}
        makeTableTitles();
        return "[OK] \nCreated new table: "+newTableName+"\n" + newTable.getTableAsString();
    }
    private void createMaxIdFile(Database database) throws IOException, GenericException {
        File newFile = new File("databases/"+database.Name+"/MaxIds.txt");
        if(!newFile.createNewFile()){throw new GenericException( "[ERROR] : Unable to create new table");}
        database.setMaxIDFile(newFile);
    }
    private void makeTable() throws GenericException, IOException {
        newTableName = ActiveToken;
        testNotKeyword();
        File newTableFile = new File("databases/"+DBServer.activeDatabase.Name+"/"+ newTableName+".tab");
        if(!newTableFile.createNewFile()){throw new GenericException( "[ERROR] : Unable to create new table");}
        newTable = new Table(ActiveToken,newTableFile,DBServer.activeDatabase);
        DBServer.activeDatabase.tables.add(newTable);
        newTable.addColumn("id");
        newTable.updateTableString();
        newTable.writeToDisk();
        DBServer.activeDatabase.idList.createNewTableEntry(newTableName);
    }
    private void makeTableTitles() throws GenericException {
        IncrementToken();
        if(!isPlainText()){throw new GenericException("[ERROR] : Expected plain text column title");}
        testNotKeyword();
        newTable.addColumn(ActiveToken);
        IncrementToken();
        if(ActiveToken.equals(",")){makeTableTitles();return;}
        newTable.writeToDisk();
        if(!ActiveToken.equals(")")){throw new GenericException("[ERROR] : Invalid column name list");}
    }
}
