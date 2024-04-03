package edu.uob.Handlers.Conditions;
import edu.uob.DataStructures.DataRow;
import edu.uob.Utilities.StringUtils;
import edu.uob.DataStructures.Table;
import edu.uob.Utilities.valueType;

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
        LIKE,
    }
    comparison comparisonType;
    valueType typeOfValue;
    int ValueInt=0;
    boolean ValueBoolean=false;
    float ValueFloat=0;
    String ValueString;
    Table activeTable;
    int attributeIndex;
    public ArrayList<String> validIDList;
    public Comparison(){}
    public boolean evaluate(){
        validIDList = new ArrayList<>();
        for(DataRow dataRow : activeTable.DataList){
            switch(typeOfValue){
                case INTEGER -> testInteger(dataRow);
                case BOOLEAN -> testBoolean(dataRow);
                case FLOAT -> testFloat(dataRow);
                case STRING -> testString(dataRow);
            }
        }
        return true;
    }
    private void testString(DataRow dataRow){
        switch (comparisonType) {
            case EQUALTO -> {
                if (dataRow.DataPoints.get(attributeIndex).equalsIgnoreCase(ValueString)) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case NOTEQUALTO -> {
                if (!dataRow.DataPoints.get(attributeIndex).equalsIgnoreCase(ValueString)) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case LIKE -> {
                if (dataRow.DataPoints.get(attributeIndex).equals(ValueString)){
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
        }
    }
    private void testBoolean(DataRow dataRow){
        switch (comparisonType) {
            case EQUALTO -> {
                if (Boolean.parseBoolean(dataRow.DataPoints.get(attributeIndex)) == ValueBoolean) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case NOTEQUALTO -> {
                if (Boolean.parseBoolean(dataRow.DataPoints.get(attributeIndex)) != ValueBoolean) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case LIKE -> {
                if (dataRow.DataPoints.get(attributeIndex).equals(ValueString)){
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
        }
    }
    private void testFloat(DataRow dataRow){
        switch (comparisonType) {
            case EQUALTO -> {
                if (Float.parseFloat(dataRow.DataPoints.get(attributeIndex)) == ValueFloat) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case NOTEQUALTO -> {
                if (Float.parseFloat(dataRow.DataPoints.get(attributeIndex)) != ValueFloat) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case LESSTHAN -> {
                if (Float.parseFloat(dataRow.DataPoints.get(attributeIndex)) < ValueFloat) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case GREATERTHAN -> {
                if (Float.parseFloat(dataRow.DataPoints.get(attributeIndex)) > ValueFloat) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case LESSTHANEQUALTO -> {
                if (Float.parseFloat(dataRow.DataPoints.get(attributeIndex)) <= ValueFloat) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case GREATERTHANEQUALTO -> {
                if (Float.parseFloat(dataRow.DataPoints.get(attributeIndex)) >= ValueFloat) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case LIKE -> {
                if (dataRow.DataPoints.get(attributeIndex).equals(ValueString)){
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
        }
    }
    private void testInteger(DataRow dataRow){
        switch (comparisonType) {
            case EQUALTO -> {
                if (Integer.parseInt(dataRow.DataPoints.get(attributeIndex)) == ValueInt) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case NOTEQUALTO -> {
                if (Integer.parseInt(dataRow.DataPoints.get(attributeIndex)) != ValueInt) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case LESSTHAN -> {
                if (Integer.parseInt(dataRow.DataPoints.get(attributeIndex)) < ValueInt) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case GREATERTHAN -> {
                if (Integer.parseInt(dataRow.DataPoints.get(attributeIndex)) > ValueInt) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case LESSTHANEQUALTO -> {
                if (Integer.parseInt(dataRow.DataPoints.get(attributeIndex)) <= ValueInt) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case GREATERTHANEQUALTO -> {
                if (Integer.parseInt(dataRow.DataPoints.get(attributeIndex)) >= ValueInt) {
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
            case LIKE -> {
                if (dataRow.DataPoints.get(attributeIndex).equals(ValueString)){
                    validIDList.add(dataRow.DataPoints.get(0));
                }
            }
        }
    }
    public boolean addValue(String value){
        ValueString = value;
        StringBuilder bufferBuilder = new StringBuilder();
        if(StringUtils.isBooleanLiteral(value)){
            typeOfValue = valueType.BOOLEAN;
            ValueBoolean = value.equalsIgnoreCase("TRUE");
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
        if(StringUtils.isStringLiteral(value,bufferBuilder)){
            value = bufferBuilder.toString();
            typeOfValue = valueType.STRING;
            ValueString = value;
            return true;
        }
        return false;
    }
    public boolean addAttribute(Table table,int titleIndex){
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
            case "==", "=" -> {
                comparisonType = comparison.EQUALTO;
                yield true;
            }
            case "!=" -> {
                comparisonType = comparison.NOTEQUALTO;
                yield true;
            }
            case "LIKE" -> {
                comparisonType = comparison.LIKE;
                yield true;
            }
            default -> false;
        };
    }
}
