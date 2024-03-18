package edu.uob;


import java.util.ArrayList;
public class DataRow {

    private int size;
    String[] DataPoints;
    private int maxSize;
    private String DataRowAsString;
    ArrayList<valueType> datapointsTypes;

    DataRow(String Input,int DesiredSize){
        size =0;
        maxSize = 10;
        DataPoints = new String[maxSize];
        DataRowAsString = "";
        MakeRow(Input);
        CheckSize(DesiredSize);
    }
    public void addValue(String datapoint){
        DataPoints[size++] = datapoint;
    }
    public void removeValue(int i){
        while(i<size){
            DataPoints[i++] = DataPoints[i+1];
        }
        size--;
    }

    private void MakeRow(String Input){
        datapointsTypes = new ArrayList<valueType>();
        String datapoint = "";
        char currentChar;
        for(int i=0;i<Input.length();i++) {
            currentChar = Input.charAt(i);
            if (currentChar != '\t') {
                datapoint += currentChar;
            } else {
                //INSERT DATAPOINT
                DataPoints[size++] = datapoint;
                addTypeList(datapoint);
                DataRowAsString += datapoint +" ";
                datapoint = "";
                if(size>=maxSize){
                    makeArrayBigger();
                }
            }
        }
        DataRowAsString += "\n";
    }
    private void addTypeList(String datapoint){
        if(isBooleanLiteral(datapoint)){
            datapointsTypes.add(valueType.BOOLEAN);
            System.out.println("BOOLEAN"+datapoint);
            return;
        }
        if(isIntegerLiteral(datapoint)){
            datapointsTypes.add(valueType.INTEGER);
            System.out.println("INT"+datapoint);
            return;
        }
        if(isFloatLiteral(datapoint)){
            datapointsTypes.add(valueType.FLOAT);
            System.out.println("FLOAT"+datapoint);
            return;
        }
        datapointsTypes.add(valueType.STRING);
        System.out.println("STRING"+datapoint);
    }
    private void makeArrayBigger(){
        String[] newList = new String[maxSize*2];
        System.arraycopy(DataPoints, 0, newList, 0, DataPoints.length);
        DataPoints = newList;
    }
    private void CheckSize(int DesiredSize){
        if(DesiredSize!=size){
            throw new RuntimeException("ERROR: inconsistent table width during creation");
        }
    }
    public String getDataRowAsString(){
        return DataRowAsString;
    }
    public boolean isBooleanLiteral(String value) {
        if (value.equals("TRUE")){return true;}
        return value.equals("FALSE");
    }
    public boolean isFloatLiteral(String value){
        if (notValFirstValue(value)) return false;
        boolean passedPeriod=false;
        for(int i=1;i<value.length();i++){
            char character = value.charAt(i);
            if(!(Character.isDigit(character)||character=='.')){return false;}
            if(character=='.') {if(!passedPeriod) {passedPeriod = true;} else {return false;}
            }
        }
        return true;
    }
    public boolean isIntegerLiteral(String value){
        if (notValFirstValue(value)) return false;
        for(int i=1;i<value.length();i++){
            char character = value.charAt(i);
            if(!(Character.isDigit(character))){return false;}
        }
        return true;
    }
    public boolean isStringLiteral(String value){
        if(value.charAt(0)!='\''){return false;}
        String bufferValue="";
        for(int i=1;i<value.length()-1;i++) {
            char character = value.charAt(i);
            if(character==34||character==39||character==124){return false;}
        }
        return value.charAt(0)!='\'';

    }
    private boolean notValFirstValue(String value) {
        char firstValue =value.charAt(0);
        if(firstValue=='+'){return false;}
        else if (firstValue=='-') {return false;}
        else return !Character.isDigit(firstValue);
    }
}
