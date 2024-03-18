package edu.uob;
import java.util.ArrayList;

public class Comparison {
    ArrayList<String> comparisonTokens;
    enum comparison{
        LESSTHAN,
        GREATERTHAN,
        LESSTHANEQUALTO,
        GREATERTHANEQUALTO,
        EQUALTO,
        NOTEQUALTO,
    };
    comparison comparisonType;
    String attribute;
    valueType typeOfValue;
    int ValueInt=0;
    boolean ValueBoolean=false;
    float ValueFloat=0;
    String ValueString;
    Table activeTable;
    int attributeIndex;
    public ArrayList<String> validIDList;
    Comparison(){}
    public boolean evaluate(){
        validIDList = new ArrayList<String>();
        for(DataRow dataRow : activeTable.DataList){
            if(typeOfValue!=dataRow.datapointsTypes.get(attributeIndex)){return false;}
            if((typeOfValue==valueType.BOOLEAN || typeOfValue==valueType.STRING )&& !(comparisonType==comparison.EQUALTO||comparisonType==comparison.NOTEQUALTO)){return false;}
            switch(typeOfValue){
                case INTEGER -> testInteger(dataRow);
                case BOOLEAN -> testBoolean(dataRow);
                case FLOAT -> testFloat(dataRow);
                case STRING -> testString(dataRow);
            }
        }
        return true;
    }
    public void printIDs() {
        for (String ID : validIDList) {
            System.out.println(ID);
        }
    }
    private void testString(DataRow dataRow){
        switch (comparisonType) {
            case EQUALTO -> {
                if (dataRow.DataPoints[attributeIndex].equals(ValueString)) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case NOTEQUALTO -> {
                if (!dataRow.DataPoints[attributeIndex].equals(ValueString)) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
        }
    }
    private void testBoolean(DataRow dataRow){
        switch (comparisonType) {
            case EQUALTO -> {
                if (Boolean.parseBoolean(dataRow.DataPoints[attributeIndex]) == ValueBoolean) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case NOTEQUALTO -> {
                if (Boolean.parseBoolean(dataRow.DataPoints[attributeIndex]) != ValueBoolean) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
        }
    }
    private void testFloat(DataRow dataRow){
        switch (comparisonType) {
            case EQUALTO -> {
                if (Float.parseFloat(dataRow.DataPoints[attributeIndex]) == ValueFloat) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case NOTEQUALTO -> {
                if (Float.parseFloat(dataRow.DataPoints[attributeIndex]) != ValueFloat) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case LESSTHAN -> {
                if (Float.parseFloat(dataRow.DataPoints[attributeIndex]) < ValueFloat) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case GREATERTHAN -> {
                if (Float.parseFloat(dataRow.DataPoints[attributeIndex]) > ValueFloat) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case LESSTHANEQUALTO -> {
                if (Float.parseFloat(dataRow.DataPoints[attributeIndex]) <= ValueFloat) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case GREATERTHANEQUALTO -> {
                if (Float.parseFloat(dataRow.DataPoints[attributeIndex]) >= ValueFloat) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
        }
    }
    private void testInteger(DataRow dataRow){
        switch (comparisonType) {
            case EQUALTO -> {
                if (Integer.parseInt(dataRow.DataPoints[attributeIndex]) == ValueInt) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case NOTEQUALTO -> {
                if (Integer.parseInt(dataRow.DataPoints[attributeIndex]) != ValueInt) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case LESSTHAN -> {
                if (Integer.parseInt(dataRow.DataPoints[attributeIndex]) < ValueInt) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case GREATERTHAN -> {
                if (Integer.parseInt(dataRow.DataPoints[attributeIndex]) > ValueInt) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case LESSTHANEQUALTO -> {
                if (Integer.parseInt(dataRow.DataPoints[attributeIndex]) <= ValueInt) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
            case GREATERTHANEQUALTO -> {
                if (Integer.parseInt(dataRow.DataPoints[attributeIndex]) >= ValueInt) {
                    validIDList.add(dataRow.DataPoints[0]);
                }
                return;
            }
        }
    }
    public boolean addValue(String value){
        String bufferValue = "";
        if(StringUtils.isBooleanLiteral(value)){
            typeOfValue = valueType.BOOLEAN;
            ValueBoolean = value.equals("TRUE");
            return true;
        }
        if(StringUtils.isIntegerLiteral(value)){
            typeOfValue = valueType.INTEGER;
            ValueInt = Integer.parseInt(value);
            return true;
        }
        if(StringUtils.isFloatLiteral(value)){
            typeOfValue = valueType.FLOAT;
            ValueFloat = Float.parseFloat(value);
            return true;
        }
        if(StringUtils.isStringLiteral(value,bufferValue)){
            typeOfValue = valueType.STRING;
            ValueString = bufferValue;
            return true;
        }
        return false;
    }
    public boolean addAttribute(String title,Table table,int titleIndex){
        attribute=title;
        attributeIndex=titleIndex;
        activeTable = table;
        return true;
    }
    public boolean addType(String comparator){
        return switch (comparator) {
            case "<" -> {
                comparisonType = comparison.LESSTHAN;
                yield true;
            }
            case ">" -> {
                comparisonType = comparison.GREATERTHAN;
                yield true;
            }
            case "<=" -> {
                comparisonType = comparison.LESSTHANEQUALTO;
                yield true;
            }
            case ">=" -> {
                comparisonType = comparison.GREATERTHANEQUALTO;
                yield true;
            }
            case "==" -> {
                comparisonType = comparison.EQUALTO;
                yield true;
            }
            case "!=" -> {
                comparisonType = comparison.NOTEQUALTO;
                yield true;
            }
            default -> false;
        };
    }
}
