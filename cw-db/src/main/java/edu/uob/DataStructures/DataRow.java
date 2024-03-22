package edu.uob.DataStructures;


import edu.uob.Utilities.GenericException;
import edu.uob.Utilities.StringUtils;
import edu.uob.Utilities.valueType;

import java.util.ArrayList;
public class DataRow {

    private int size;
    public ArrayList<String> DataPoints;
    private String DataRowAsString;
    public ArrayList<valueType> datapointsTypes;

    DataRow(){
        size =0;
        DataPoints = new ArrayList<String>();
        DataRowAsString = "";
    }
    public void initialise(String Input,int DesiredSize,int positionInTable,boolean fromFile) throws GenericException {
        MakeRow(Input,positionInTable,fromFile);
        CheckSize(DesiredSize);
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
    public void checkTypes(ArrayList<valueType> inputList) throws GenericException {
        valueType type;
        String value;
        for(int i=0;i<size;i++){
            value = DataPoints.get(i);
            type = inputList.get(i);
            if(!type.equals(StringUtils.checkType(value))){}//throw new GenericException("[ERROR] : Incorrect type insertion");}
        }
        datapointsTypes = inputList;
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
    private void MakeRow(String Input,int positionInTable,boolean fromFile){
        datapointsTypes = new ArrayList<valueType>();
        String datapoint = "";
        if(!fromFile) {
            DataPoints.add(Integer.toString(positionInTable));
            datapointsTypes.add(valueType.INTEGER);
        }
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
    private void CheckSize(int DesiredSize) throws GenericException{
        if(DesiredSize!=DataPoints.size()){throw new GenericException("[ERROR] : Invalid datarow size");}
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
    public String getSpecificDataRowValues(ArrayList<Integer> indicies){
        String returnString = "";
        for(Integer i : indicies){
            returnString += DataPoints.get(i) + "\t";
        }
        returnString += "\n";
        return returnString;
    }
}
