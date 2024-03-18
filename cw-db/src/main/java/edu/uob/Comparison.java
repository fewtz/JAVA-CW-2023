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
    ArrayList<String> validIDList;
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
        return isBooleanLiteral(value)|| isIntegerLiteral(value) || isFloatLiteral(value) || isStringLiteral(value);
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
    public boolean isIntegerLiteral(String value){
        if (notValFirstValue(value)) return false;
        for(int i=1;i<value.length();i++){
            char character = value.charAt(i);
            if(!(Character.isDigit(character))){return false;}
        }
        typeOfValue = valueType.INTEGER;
        ValueInt = Integer.parseInt(value);
        return true;
    }

    public boolean isBooleanLiteral(String value) {
        if (value.equals("TRUE")){
            ValueBoolean = true;
            typeOfValue = valueType.BOOLEAN;
            return true;
        }
        if (value.equals("FALSE")){
            ValueBoolean = false;
            typeOfValue = valueType.BOOLEAN;
            return true;
        }
        return false;
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
        typeOfValue = valueType.FLOAT;
        ValueFloat = Float.parseFloat(value);
        return true;
    }
    public boolean isStringLiteral(String token){
        if(token.charAt(0)!='\''){return false;}
        String bufferValue="";
        for(int i=1;i<token.length()-1;i++) {
            char character = token.charAt(i);
            if(character==34||character==39||character==124){return false;}
            bufferValue+=character;
        }
        if(token.charAt(0)!='\''){return false;}
        typeOfValue = valueType.STRING;
        ValueString = bufferValue;
        return true;
    }
    private boolean notValFirstValue(String value) {
        char firstValue =value.charAt(0);
        if(firstValue=='+'){return false;}
        else if (firstValue=='-') {return false;}
        else return !Character.isDigit(firstValue);
    }
}
