package edu.uob.Handlers.SubHandlers;

import edu.uob.DataStructures.DataRow;
import edu.uob.Handlers.Conditions.ConditionHandler;
import edu.uob.DataStructures.Table;
import edu.uob.Utilities.GenericException;

import java.util.ArrayList;

public class SelectHandler extends ConditionHandler {
    Table activeTable;
    ArrayList<Integer> attributeIndexList;
    ArrayList<String> attributeStringList = new ArrayList<String>();
    boolean wildList=false;
    public SelectHandler(ArrayList<String> Input) {
        tokens = Input;
    }
    public String handleSelect() throws GenericException {
        CurrentToken =0;
        checkAttributeList();
        compareToken(ActiveToken,"FROM");
        IncrementToken();
        activeTable = isTable();
        IncrementToken();
        generateAttributeIndexes();
        if(ActiveToken.equalsIgnoreCase(";")){return pushAll();}
        compareToken(ActiveToken,"WHERE");
        IncrementToken();
        checkValidConditions(activeTable);
        return pushSelected();
    }
    private void generateAttributeIndexes() throws GenericException {
        attributeIndexList = new ArrayList<>();
        if(!wildList) {
            for (String attribute : attributeStringList) {
                checkAttributeExists(attribute,attributeIndexList,activeTable);
            }
        }
        else{
            for(int i=0;i<activeTable.getNumberOfColumns();i++){
                attributeIndexList.add(i);
            }
        }
    }
    private String pushSelected() throws GenericException {
        String output = "[OK] \n";
        output += activeTable.getSpecificColumnNames(attributeIndexList);
        for(DataRow dataRow : validRows){
            output += dataRow.getSpecificDataRowValues(attributeIndexList);
        }
        return output;
    }
    private String pushAll() throws GenericException {
        String output = "[OK]\n";
        output += activeTable.getSpecificColumnNames(attributeIndexList);
        for(DataRow dataRow : activeTable.DataList){
            output += dataRow.getSpecificDataRowValues(attributeIndexList);
        }
        return output;
    }
    private void checkAttributeList() throws GenericException {
        IncrementToken();
        if (ActiveToken.equals("*")) {
            wildList = true;
            IncrementToken();
            return ;
        }
        if (!isPlainText()) {throw new GenericException("[ERROR] : Invalid attribute list");}
        attributeStringList.add(ActiveToken);
        IncrementToken();
        if (ActiveToken.equals(",")) {
            checkAttributeList();
        }
    }
}
