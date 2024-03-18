package edu.uob;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import java.io.*;


public class Table {
    private int numberOfRows;
    private int numberOfColumns;
    public String[] columnNames;
    private int colNamesSize;
    private BufferedReader buffReader;
    public ArrayList<DataRow> DataList;
    private String TableName;
    private String TableAsString;
    public File tableFile;
    public ArrayList<valueType> typesOfValues;
    public boolean isEmpty = true;
    //linked list....
    Table(String Name,File inputFile){
        TableName = Name;
        tableFile = inputFile;
        numberOfColumns = 0;
        numberOfRows = 0;
        DataList = new ArrayList<DataRow>();
        colNamesSize = 10;
        columnNames = new String[colNamesSize];
        TableAsString = "";
    }
    public String getSpecificColumnNames(ArrayList<Integer> indecies){
        String returnString = "";
        for(Integer i : indecies){
            returnString += columnNames[i] + " ";
        }
        returnString+="\n";
        return returnString;
    }
    public void updateTableString(){
        String newTableString = "";
        for(String title : columnNames){
            if(title!=null) {
                newTableString += title + " ";
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
    public String getName(){
        return TableName;
    }
    public void addColumn(String title){
        columnNames[numberOfColumns++] = title;

        for(DataRow datarow : DataList){
            datarow.addValue("*");
        }
        updateTableString();
    }
    public void removeColumn(String title){
        for(int i=0;i<numberOfColumns;i++){
            String colName = columnNames[i];
            if(colName.equals(title)){
                removeFromList(columnNames,i,numberOfColumns--);
                removeDataPoints(i);
            }
        }

        updateTableString();
    }
    public boolean insertValues(String values){
        DataRow dataRow = new DataRow();
        if(!dataRow.initialise(values, numberOfColumns)){ return false;}
        if(!isEmpty) {
            if(!dataRow.checkTypes(typesOfValues)){return false;}
        }
        isEmpty = false;
        DataList.add(dataRow);
        updateTableString();
        return true;
    }
    private void removeDataPoints(int i){
        for(DataRow datarow : DataList){
            datarow.removeValue(i);
        }
        updateTableString();
    }
    private void removeFromList(String[] list, int i,int listSize){
        while(i<listSize-1) {
            list[i++] = list[i+1];
        }
        updateTableString();
    }
    public void createTableFromFiles(BufferedReader inpBuffReader,File inputFile) throws IOException {
        buffReader = inpBuffReader;
        makeColumns(buffReader.readLine());
        readData();
    }
    public int getNumberOfColumns() {
        return numberOfColumns;
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
                columnNames[numberOfColumns++] = colName;
                TableAsString += colName + " ";
                colName = "";
                if(numberOfColumns>=colNamesSize){
                    makeNamesArrayBigger();
                }
            }
        }
        TableAsString += "\n";
    }
    private void makeNamesArrayBigger(){
        String[] newList = new String[colNamesSize*2];
        System.arraycopy(columnNames, 0, newList, 0, columnNames.length);
        columnNames = newList;
    }
    private void readData() throws IOException {
        String nextLine;
        while(true){
            nextLine = buffReader.readLine();
            if(nextLine == null){
                return;
            }
            AddRow(nextLine);
        }
    }
    private void AddRow(String Input){
        DataRow newRow = new DataRow();
        newRow.initialise(Input,numberOfColumns);
        TableAsString += newRow.getDataRowAsString();
        DataList.add(newRow);
        numberOfRows++;
        updateTableString();
        isEmpty = false;
        typesOfValues = DataList.get(0).getTypeList();
        newRow.setTypeList(typesOfValues);
    }
    public String getTableAsString(){
        return TableAsString;
    }
}
