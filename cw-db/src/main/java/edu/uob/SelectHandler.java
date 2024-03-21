package edu.uob;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SelectHandler extends ConditionHandler{
    Table activeTable;
    ArrayList<Integer> attributeIndexList;
    ArrayList<String> attributeStringList = new ArrayList<String>();
    boolean wildList=false;
    boolean isList=false;
    SelectHandler(ArrayList<String> Input) {
        tokens = Input;
    }
    public String handleSelect(){
        CurrentToken =0;
        if(!isAttributeList()){return "[ERROR] : That is not a valid attribute list";}
        IncrementToken();
        if(!ActiveToken.equals("FROM")){return "[ERROR] : That is not a valid select command";}
        IncrementToken();
        if((activeTable = isTable(activeTable))==null){return "[ERROR] : That is not a valid table name";}
        IncrementToken();
        if(!generateAttributeIndexes()){return "[ERROR] : That attribute is not in scope";}
        if(ActiveToken.equals(";")){return pushAll();}
        if(!ActiveToken.equals("WHERE")){return "[ERROR] : That is not a valid select command";}
        IncrementToken();
        if(!validConditions()){return "[ERROR] : That is not a valid condition";}
        return pushSelected();
    }
    private boolean generateAttributeIndexes(){
        attributeIndexList = new ArrayList<Integer>();
        boolean foundValue;
        if(!wildList) {
            for (String attribute : attributeStringList) {
                foundValue = false;
                for (int i = 0; i < activeTable.getNumberOfColumns(); i++) {
                    String title = activeTable.columnNames[i];
                    if (attribute.equals(title)) {
                        attributeIndexList.add(i);
                        foundValue = true;
                    }
                }
                if (!foundValue) {
                    return false;
                }
            }
        }
        else{
            for(int i=0;i<activeTable.getNumberOfColumns();i++){
                attributeIndexList.add(i);
            }
        }
        return true;
    }
    private String pushSelected(){
        String output = "[OK] \n";
        output += activeTable.getSpecificColumnNames(attributeIndexList);
        for(DataRow dataRow : validRows){
            output += dataRow.getSpecificDataRowValues(attributeIndexList);
        }
        return output;
    }
    private String pushAll(){
        String output = "[OK]\n";
        output += activeTable.getSpecificColumnNames(attributeIndexList);
        for(DataRow dataRow : activeTable.DataList){
            output += dataRow.getSpecificDataRowValues(attributeIndexList);
        }
        return output;
    }
    private boolean isAttributeList(){
        IncrementToken();
        if(!isList) {
            if (ActiveToken.equals("*")) {
                return wildList = true;
            }
            if (isPlainText()) {
                attributeStringList.add(ActiveToken);
                return true;
            }
        }
        if(ActiveToken.equals("(")){isList = true;}
        if(isList) {
            if (!isPlainText()) {
                return false;
            }
            attributeStringList.add(ActiveToken);
            IncrementToken();
            if (ActiveToken.equals(",")) {
                return isAttributeList();
            }
            return ActiveToken.equals(")");
        }
        return false;
    }
    public boolean validConditions(){
        if(!parseConditions()){return false;}
        validRows = new ArrayList<DataRow>();
        for(DataRow dataRow : activeTable.DataList){
            for(String validID : comparisonsList.get(0).validIDList){
                if(dataRow.DataPoints.get(0).equals(validID)){
                    validRows.add(dataRow);
                }
            }
        }
        return true;
    }
}
