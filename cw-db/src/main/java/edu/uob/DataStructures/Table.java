package edu.uob.DataStructures;


import edu.uob.DBServer;
import edu.uob.Utilities.GenericException;
import edu.uob.Utilities.valueType;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.Serial;

import java.io.*;
public class Table implements java.io.Serializable{
    @Serial private static final long serialVersionUID = 2;
    private int numberOfRows;
    private int numberOfColumns;
    public ArrayList<String> columnNames;
    public ArrayList<DataRow> DataList;
    private String TableName;
    private String TableAsString;
    public File tableFile;
    public ArrayList<valueType> typesOfValues;
    public boolean isEmpty = true;
    private int highestID=-1;
    private Database parentDatabase;

    //linked list....
    public Table(String Name, File inputFile,Database inpDatabase) throws IOException {
        TableName = Name;
        parentDatabase = inpDatabase;
        tableFile = inputFile;
        numberOfColumns = 0;
        numberOfRows = 0;
        DataList = new ArrayList<>();
        columnNames = new ArrayList<>();
        TableAsString = "";
    }
    public String getName(){
        return TableName;
    }
    public int getNumberOfColumns() {
        return columnNames.size();
    }
    public String getTableAsString(){
        return TableAsString;
    }

    public void updateTableString(){
        String newTableString = "";
        for(String title : columnNames){
            if(title!=null) {
                newTableString += title + "\t";
            }
        }
        newTableString+="\n";
        for(DataRow row : DataList){
            row.updateRowString();
            newTableString+=row.getDataRowAsString();
            newTableString+="\n";
        }
        TableAsString = newTableString;
    }
    public void createTableFromFiles(BufferedReader inpBuffReader,File inputFile) throws IOException, GenericException {
        makeColumns(inpBuffReader.readLine());
        tableFile = inputFile;
        TableName = TableName.substring(0,TableName.length()-4);
        readData(inpBuffReader);
        readHighestID();
    }
    private void readHighestID() throws GenericException {
        highestID =  parentDatabase.idList.getMaxID(TableName);
    }
    public int getMaxId(){
        return highestID;
    }

    private void makeColumns(String firstLine){
        String colName = "";
        char currentChar;
        if(firstLine==null){
            return;
        }
        for(int i=0;i<firstLine.length();i++){
            currentChar = firstLine.charAt(i);
            if (currentChar != '\t'&& currentChar!='\n') {
                colName += currentChar;
            }
            else{
                if(!colName.isEmpty()) {
                    columnNames.add(colName);
                    TableAsString += colName + "\t";
                    colName = "";
                }
            }
        }
        if(!colName.isEmpty()) {
            columnNames.add(colName);
            TableAsString += colName + "\t";
        }
        TableAsString += "\n";
        numberOfColumns = columnNames.size();
    }
    private void readData(BufferedReader buffReader) throws IOException, GenericException {
        String nextLine;
        while(true){
            nextLine = buffReader.readLine();
            if(nextLine == null){
                return;
            }
            AddRow(nextLine);
        }
    }
    private void AddRow(String Input) throws GenericException {
        DataRow newRow = new DataRow();
        newRow.initialise(Input,numberOfColumns,++highestID,true);
        TableAsString += newRow.getDataRowAsString();
        DataList.add(newRow);
        numberOfRows++;
        updateTableString();
        isEmpty = false;
        newRow.setTypeList((newRow.getTypeList()));
    }
    public void removeRow(DataRow toRemove){
        DataList.remove(toRemove);
        numberOfRows--;
    }
    public void addColumn(String title){
        columnNames.add(title);
        numberOfColumns++;
        for(DataRow datarow : DataList){
            datarow.addValue("*");
        }
        updateTableString();
    }
    public void removeColumn(String title){
        for(int i=0;i<columnNames.size();i++){
            String colName = columnNames.get(i);
            if(colName.equals(title)){
                columnNames.remove(colName);
                removeDataPoints(i);
            }
        }
        numberOfColumns--;
        updateTableString();
    }
    private void removeDataPoints(int i){
        for(DataRow datarow : DataList){
            datarow.removeValue(i);
        }
        updateTableString();
    }
    public void insertValues(String values) throws GenericException {
        DataRow dataRow = new DataRow();
        dataRow.initialise(values, numberOfColumns,++highestID,false);
        if(!isEmpty) {
            //dataRow.checkTypes(typesOfValues);
        }
        isEmpty = false;
        DataList.add(dataRow);
        updateTableString();
        parentDatabase.idList.setMaxID(TableName,highestID);
    }
    public String getSpecificColumnNames(ArrayList<Integer> indecies){
        String returnString = "";
        for(Integer i : indecies){
            returnString += columnNames.get(i) + " ";
        }
        returnString+="\n";
        return returnString;
    }
    public void writeToDisk() throws GenericException{
        updateTableString();
        try {
            FileWriter fileWriter = new FileWriter(tableFile,false);
            fileWriter.write(TableAsString);
            fileWriter.close();
        }catch(Exception IOException){
            throw new GenericException("[ERROR] : Unable to write to disk");
        }
    }
    public void joinTable(Table joiningTable,int keyAttribute,int foreignKeyAttribute) throws GenericException {
        columnNames.replaceAll(s -> TableName + "." + s);
        columnNames.remove(keyAttribute);
        if(keyAttribute==0) {
            columnNames.add(0, "id");
        }else{columnNames.set(0, "id");}
        int newId=0;
        boolean valueFound;
         for(DataRow dataRow : DataList){
             valueFound = false;
             String foreignKey = dataRow.getSpecificValue(keyAttribute);
             for(DataRow foreignRow : joiningTable.DataList){
                 String foreignValue = foreignRow.getSpecificValue(foreignKeyAttribute);
                 if(valueFound){
                    throw new GenericException("[ERROR] : Primary and foreign key has multiple matches in columns");
                 }
                 if(foreignKey.equals(foreignValue)){
                     dataRow.combine(foreignRow,keyAttribute,foreignKeyAttribute,newId++);
                     valueFound = true;
                 }
             }
             if(!valueFound){
                 DataList.remove(dataRow);
             }
         }
        for(String title : joiningTable.columnNames){
            int titleIndex = joiningTable.columnNames.indexOf(title);
            if(titleIndex!=0 && titleIndex!=foreignKeyAttribute){
                columnNames.add(joiningTable.TableName+"."+title);
            }
        }
        updateTableString();
    }
}
