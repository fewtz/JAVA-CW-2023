package edu.uob.DataStructures;


import edu.uob.Utilities.GenericException;
import edu.uob.Utilities.valueType;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import java.io.*;


public class Table {
    private int numberOfRows;
    private int numberOfColumns;
    public ArrayList<String> columnNames;
    private BufferedReader buffReader;
    public ArrayList<DataRow> DataList;
    private String TableName;
    private String TableAsString;
    public File tableFile;
    public ArrayList<valueType> typesOfValues;
    public boolean isEmpty = true;
    //linked list....
    public Table(String Name, File inputFile){
        TableName = Name;
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
        return numberOfColumns;
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
        buffReader = inpBuffReader;
        makeColumns(buffReader.readLine());
        TableName = TableName.substring(0,TableName.length()-4);
        readData();
    }
    private void makeColumns(String firstLine){
        String colName = "";
        char currentChar;
        if(firstLine==null){
            return;
        }
        for(int i=0;i<firstLine.length();i++){
            currentChar = firstLine.charAt(i);
            if (currentChar != '\t') {
                colName += currentChar;
            }
            else{
                columnNames.add(colName);
                TableAsString += colName + "\t";
                colName = "";
            }
        }
        TableAsString += "\n";
        numberOfColumns = columnNames.size();
    }
    private void readData() throws IOException, GenericException {
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
        newRow.initialise(Input,numberOfColumns,numberOfRows,true);
        TableAsString += newRow.getDataRowAsString();
        DataList.add(newRow);
        numberOfRows++;
        updateTableString();
        isEmpty = false;
        newRow.setTypeList((newRow.getTypeList()));
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
        for(int i=0;i<numberOfColumns;i++){
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
        dataRow.initialise(values, numberOfColumns,numberOfRows++,false);
        if(!isEmpty) {
            dataRow.checkTypes(typesOfValues);
        }
        isEmpty = false;
        DataList.add(dataRow);
        updateTableString();
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
}
