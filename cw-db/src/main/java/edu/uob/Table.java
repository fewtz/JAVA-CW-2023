package edu.uob;

import java.io.BufferedReader;

public class Table {
    private int numberOfRows;
    private int numberOfColumns;
    private String[] columnNames;
    private BufferedReader buffReader;
    //linked list....
    Table(BufferedReader inpBuffReader){
        buffReader = inpBuffReader;
        String firstLine;
        numberOfColumns = 0;
        numberOfRows = 0;
        try{
            firstLine = buffReader.readLine();
        }catch(Exception EmptyFile){
            return;
        }
        makeColumns(firstLine);
        readData();
    }

    private void makeColumns(String firstLine){
        String colName = "";
        char currentChar;
        for(int i=0;i<firstLine.length();i++){
            currentChar = firstLine.charAt(i);
            if (currentChar != '\t') {
                colName += currentChar;
            }
            else{
                columnNames[numberOfColumns++] = colName;
                colName = "";
            }
        }
    }

    private void readData(){
        String nextLine;
        while(true){
            try{
                nextLine = buffReader.readLine();
            }catch(Exception EmptyFile){
                return;
            }
            String datapoint = "";
            char currentChar;
            for(int i=0;i<nextLine.length();i++){
                currentChar = nextLine.charAt(i);
                if (currentChar != '\t') {
                    datapoint += currentChar;
                }
                else{
                    //INSERT DATAPOINT
                    datapoint = "";
                }
            }
        }

    }
}
