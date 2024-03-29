package edu.uob.DataStructures;

import java.io.Serial;
import edu.uob.Utilities.GenericException;
import edu.uob.Utilities.StringUtils;
import edu.uob.Utilities.valueType;

import java.util.ArrayList;
public class DataRow implements java.io.Serializable{
    @Serial private static final long serialVersionUID = 3;
    public ArrayList<String> DataPoints;
    private String DataRowAsString;
    public ArrayList<valueType> datapointsTypes;
    //ToDo make sure all datarows are valid on creation?

    DataRow(){
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
        for(int i=0;i<DataPoints.size();i++){
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
        while(i<DataPoints.size()-1){
            DataPoints.set(i,DataPoints.get(i+1));
            i++;
        }
        if(i==DataPoints.size()-1){
            DataPoints.remove(i);
        }
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
            if (currentChar != '\t'&& currentChar!='\n' && currentChar!='\0') {
                datapoint += currentChar;
            } else {
                if(!datapoint.isEmpty()) {
                    StringBuilder temp = new StringBuilder();
                    if (StringUtils.isStringLiteral(datapoint, temp)) {
                        datapoint = temp.toString();
                    }
                    DataPoints.add(datapoint);
                    addTypeList(datapoint);
                    DataRowAsString += datapoint + "\t";
                    datapoint = "";
                }
            }
        }
        if(!datapoint.isEmpty()) {
            StringBuilder temp = new StringBuilder();
            if (StringUtils.isStringLiteral(datapoint, temp)) {
                datapoint = temp.toString();
            }
            DataPoints.add(datapoint);
            addTypeList(datapoint);
            DataRowAsString += datapoint + "\t";
            datapoint = "";
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
    public String getSpecificDataRowValues(ArrayList<Integer> indicies) throws GenericException {
        String returnString = "";
        for(Integer i : indicies){
            if(i>=DataPoints.size()){throw new GenericException("[ERROR] : Target index is outside the size of the datarow");}
            returnString += DataPoints.get(i) + "\t";
        }
        returnString += "\n";
        return returnString;
    }
    public String getSpecificValue(int index) throws GenericException {
        if(index>=DataPoints.size()){throw new GenericException("[ERROR] : Target index is outside the size of the datarow");}
        return DataPoints.get(index);
    }
    public void combine(DataRow foreignRow,int  keyAttribute,int foreignKeyAttribute,int index){
        DataPoints.remove(keyAttribute);
        if(keyAttribute==0) {
            DataPoints.add(0, Integer.toString(index));
        }else{DataPoints.set(0, Integer.toString(index));}
        for(String value : foreignRow.DataPoints){
            int foreignIndex = foreignRow.DataPoints.indexOf(value);
            if(foreignIndex != foreignKeyAttribute && foreignIndex != 0 ){
                addValue(value);
            }
        }

        updateRowString();
    }

}
