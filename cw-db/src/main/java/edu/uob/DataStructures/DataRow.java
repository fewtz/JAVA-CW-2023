package edu.uob;


import java.util.ArrayList;
public class DataRow {

    private int size;
    ArrayList<String> DataPoints;
    private String DataRowAsString;
    ArrayList<valueType> datapointsTypes;

    DataRow(){
        size =0;
        DataPoints = new ArrayList<String>();
        DataRowAsString = "";
    }
    public boolean initialise(String Input,int DesiredSize,int positionInTable){
        MakeRow(Input,positionInTable);
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
            value = DataPoints.get(i);
            type = inputList.get(i);
            if(!type.equals(StringUtils.checkType(value))){return false;}
        }
        datapointsTypes = inputList;
        return true;
    }
    public void addValue(String datapoint){
        DataPoints.add(datapoint);
    }
    public void removeValue(int i){
        while(i<size){
            DataPoints.set(i,DataPoints.get(i+1));
            i++;
        }
        size--;
    }
    private void MakeRow(String Input,int positionInTable){
        datapointsTypes = new ArrayList<valueType>();
        String datapoint = "";
        DataPoints.add(Integer.toString(positionInTable));
        datapointsTypes.add(valueType.INTEGER);
        char currentChar;
        for(int i=0;i<Input.length();i++) {
            currentChar = Input.charAt(i);
            if (currentChar != '\t') {
                datapoint += currentChar;
            } else {
                //INSERT DATAPOINT
                StringBuilder temp = new StringBuilder();
                if(StringUtils.isStringLiteral(datapoint,temp)){datapoint=temp.toString();}
                DataPoints.add(datapoint);
                addTypeList(datapoint);
                DataRowAsString += datapoint +"\t";
                datapoint = "";
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
    private boolean CheckSize(int DesiredSize){
        return DesiredSize==DataPoints.size();
    }
    public String getDataRowAsString(){
        return DataRowAsString;
    }
    public void updateRowString(){
        String newRowString = "";
        for(String datapoint : DataPoints){
            if(datapoint!=null) {
                newRowString += datapoint + "\t";
            }
        }
        DataRowAsString = newRowString;
    }
    public String getSpecificDataRowValues(ArrayList<Integer> indecies){
        String returnString = "";
        for(Integer i : indecies){
            returnString += DataPoints.get(i) + "\t";
        }
        returnString += "\n";
        return returnString;
    }
}
