package edu.uob;


import java.util.ArrayList;
public class DataRow {

    private int size;
    String[] DataPoints;
    private int maxSize;
    private String DataRowAsString;
    ArrayList<valueType> datapointsTypes;

    DataRow(){
        size =0;
        maxSize = 10;
        DataPoints = new String[maxSize];
        DataRowAsString = "";
    }
    public boolean initialise(String Input,int DesiredSize){
        MakeRow(Input);
        return CheckSize(DesiredSize);
    }
    public void setTypeList(ArrayList<valueType> inputList){
        datapointsTypes = inputList;
    }
    public ArrayList<valueType> getTypeList(){
        for(String value : DataPoints){
            if(value!=null) {
                datapointsTypes.add(StringUtils.checkType(value));
            }
        }
        return datapointsTypes;
    }
    public boolean checkTypes(ArrayList<valueType> inputList){
        valueType type;
        String value;
        for(int i=0;i<size;i++){
            value = DataPoints[i];
            type = inputList.get(i);
            if(!type.equals(StringUtils.checkType(value))){return false;}
        }
        datapointsTypes = inputList;
        return true;
    }
    public void addValue(String datapoint){
        DataPoints[size++] = datapoint;
        System.out.println("Added value !");
    }
    public void removeValue(int i){
        while(i<size){
            DataPoints[i] = DataPoints[i+1];
            i++;
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
        if(StringUtils.isBooleanLiteral(datapoint)){
            datapointsTypes.add(valueType.BOOLEAN);
            return;
        }
        if(StringUtils.isIntegerLiteral(datapoint)){
            datapointsTypes.add(valueType.INTEGER);
            return;
        }
        if(StringUtils.isFloatLiteral(datapoint)){
            datapointsTypes.add(valueType.FLOAT);
            return;
        }
        datapointsTypes.add(valueType.STRING);
    }
    private void makeArrayBigger(){
        String[] newList = new String[maxSize*2];
        System.arraycopy(DataPoints, 0, newList, 0, DataPoints.length);
        DataPoints = newList;
    }
    private boolean CheckSize(int DesiredSize){
        return DesiredSize==size;
    }
    public String getDataRowAsString(){
        return DataRowAsString;
    }
    public void updateRowString(){
        String newRowString = "";
        for(String datapoint : DataPoints){
            if(datapoint!=null) {
                newRowString += datapoint + " ";
            }
        }
        DataRowAsString = newRowString;
    }
    public String getSpecificDataRowValues(ArrayList<Integer> indecies){
        String returnString = "";
        for(Integer i : indecies){
            returnString += DataPoints[i] + " ";
        }
        returnString += "\n";
        return returnString;
    }
}
