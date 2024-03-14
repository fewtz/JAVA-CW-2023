package edu.uob;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Table {
    private int numberOfRows;
    private int numberOfColumns;
    public String[] columnNames;
    private int colNamesSize;
    private BufferedReader buffReader;
    private ArrayList<DataRow> DataList;
    private String TableName;
    private String TableAsString;
    //linked list....
    Table(String Name){
        TableName = Name;
        numberOfColumns = 0;
        numberOfRows = 0;
        DataList = new ArrayList<DataRow>();
        colNamesSize = 10;
        columnNames = new String[colNamesSize];
        TableAsString = "";
    }
    public String getName(){
        return TableName;
    }
    public void createTableFromFiles(BufferedReader inpBuffReader) throws IOException {
        buffReader = inpBuffReader;
        makeColumns(buffReader.readLine());
        readData();
    }
    public String getTableName(){
        return TableName;
    }

    private void makeColumns(String firstLine){
        String colName = "";
        char currentChar;
        if(firstLine==null){
            throw new RuntimeException("ERROR : Read empty file\n");
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
        DataRow newRow = new DataRow(Input,numberOfColumns);
        TableAsString += newRow.getDataRowAsString();
        DataList.add(newRow);
        numberOfRows++;
    }
    public String getTableAsString(){
        return TableAsString;
    }
    public void InsertNewRow(String Input){
        AddRow(Input);
    }
    private void RemoveRow(DataRow row){
        DataList.remove(row);
    }
}
