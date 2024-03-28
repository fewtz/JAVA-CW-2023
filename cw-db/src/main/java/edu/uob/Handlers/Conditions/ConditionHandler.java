package edu.uob.Handlers.Conditions;
import edu.uob.*;
import edu.uob.DataStructures.DataRow;
import edu.uob.DataStructures.Table;
import edu.uob.Handlers.Handler;
import edu.uob.Utilities.GenericException;

import java.util.ArrayList;
public abstract class ConditionHandler extends Handler {
    enum ConditionComponent{
        BOOLEANOPERATOR,
        EXPRESSION,
        OPENBRACKET,
        CLOSEBRACKET,
    };
    String parsingToken;
    int ReturnVal;
    ArrayList<ConditionComponent> conditionList;
    int currentParsingToken;
    ConditionComponent activeComponent;
    int inUseBrackets = 0;
    int numberOfCompoents;
    int furthestComponent=0;
    public ArrayList<Comparison> comparisonsList;
    public ArrayList<DataRow> validRows;

    public void parseConditions() throws GenericException {
        conditionList = new ArrayList<ConditionComponent>();
        parsingToken = ActiveToken;
        currentParsingToken=CurrentToken+1;
        comparisonsList = new ArrayList<Comparison>();
        while(!parsingToken.equals(";")&&currentParsingToken<tokens.size()){
            if(!checkConditionComponentType()){throw new GenericException("[ERROR] : Invalid condition tokens");}
            IncrementParsingToken();
        }
        printConditionList();
        numberOfCompoents=conditionList.size();
        if(!parseCondition()){throw new GenericException("[ERROR] : Invalid condition logic");}
    }
    private void printConditionList(){
        for(ConditionComponent component : conditionList){
            switch(component){
                case BOOLEANOPERATOR -> System.out.println("BOOLEAN");
                case CLOSEBRACKET-> System.out.println("CLOSEBRACKET");
                case OPENBRACKET -> System.out.println("OPENBRACKET");
                case EXPRESSION -> System.out.println("EXPRESSION");
            }
        }
    }
    private boolean parseCondition(){
        int testing,testing2,testing3;
        testing = parseComboCondition(0);
        if(furthestComponent+1>=numberOfCompoents && inUseBrackets==0){return true;}
        testing2 = parseOpenBracket(0);
        if(furthestComponent+1>=numberOfCompoents && inUseBrackets==0){return true;}
        testing3 = parseExpression(0);
        if(testing<0 && testing2<0 && testing3<0){return false;}
        return furthestComponent+1>=numberOfCompoents && inUseBrackets==0;
    }
    private int parseOpenBracket(int currentComponent){
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if(activeComponent!=ConditionComponent.OPENBRACKET){return -1;}
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if((currentComponent=checkComboBracketAndExpression(currentComponent))<0){return -1;}
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if(activeComponent!=ConditionComponent.CLOSEBRACKET){return -1;}
        else{return currentComponent;}
    }    private int parseExpression(int currentComponent){
        currentComponent = IncrementConditionList(currentComponent);
        if(activeComponent!=ConditionComponent.EXPRESSION){return -1;}
        else{return currentComponent;}
    }
    private int parseComboCondition(int currentComponent){
        ComboComparison newComboComparison = new ComboComparison();
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if((currentComponent = checkBracketAndExpression(currentComponent-1))<0){return -1;}
        newComboComparison.addFirstComparison(comparisonsList.get(0));
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if(activeComponent!=ConditionComponent.BOOLEANOPERATOR){return -1;}
        newComboComparison.addBooleanType(getTokenFromComponentIndex(currentComponent-1));
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if((currentComponent=checkBracketAndExpression(currentComponent-1))<0){return -1;}
        newComboComparison.addSecondComparison(comparisonsList.get(1));
        comparisonsList.set(0,newComboComparison.evaluateCombo());
        comparisonsList.remove(1);
        return currentComponent;
    }
    private String getTokenFromComponentIndex(int currentComponent){
        int i = CurrentToken;
        ConditionComponent currentCondition;
        for(int j=0;j<conditionList.size();j++){
            currentCondition = conditionList.get(j);
            if(j==currentComponent){
                System.out.println(currentCondition);
                System.out.println(tokens.get(i));
                return tokens.get(i);
            }
            if(currentCondition==ConditionComponent.EXPRESSION){
                i+=2;
            }
            i++;
        }
        return "";
    }
    private int checkComboBracketAndExpression(int currentComponent){
        if((ReturnVal = parseComboCondition(currentComponent-1))>0) {
            return ReturnVal;
        }else {
            return checkBracketAndExpression(currentComponent-1);
        }
    }
    private int checkBracketAndExpression(int currentComponent){
        if((ReturnVal = parseOpenBracket(currentComponent))>0){
            return ReturnVal;
        }
        else if((ReturnVal = parseExpression(currentComponent))>0){
            return ReturnVal;
        }
        else{return -1;}
    }
    public int IncrementConditionList(int currentComponent){
        if(currentComponent>=furthestComponent){furthestComponent=currentComponent;}
        if(currentComponent>=numberOfCompoents || currentComponent<0){return -1;}
        activeComponent = conditionList.get(currentComponent);
        return currentComponent+1;
    }

    public void IncrementParsingToken(){
        parsingToken=tokens.get(currentParsingToken++);
    }
    public boolean checkConditionComponentType(){
        //I'm sure this could be improved with a stream, but i haven't been taught that yet
        if(isBooleanOperator()){conditionList.add(ConditionComponent.BOOLEANOPERATOR);return true;}
        if(isOpenBracket()){conditionList.add(ConditionComponent.OPENBRACKET);return true;}
        if(isCloseBracket()){conditionList.add(ConditionComponent.CLOSEBRACKET);return true;}
        if(isExpression()){conditionList.add(ConditionComponent.EXPRESSION);return true;}
        return false;
    }
    public boolean isExpression(){
        Comparison newComparison = new Comparison();
        if(!isAttribute(newComparison,parsingToken)){return false;}
        IncrementParsingToken();
        if(!isComparator(newComparison,parsingToken)){return false;}
        IncrementParsingToken();
        if(!isValue(newComparison,parsingToken)){return false;}
        if(!newComparison.evaluate()){return false;}
        comparisonsList.add(newComparison);
        return true;
    }
    public boolean isValue(Comparison newComparison, String token){
        return newComparison.addValue(token);
    }
    public boolean isAttribute(Comparison newComparison,String token){

            for(int i=0;i<activeTable.columnNames.size();i++){
                String title = activeTable.columnNames.get(i);
                if(token.equalsIgnoreCase(title)){
                    return newComparison.addAttribute(activeTable,i);
                }
            }
        return false;
    }
    public boolean isComparator(Comparison newComparison,String token){
        return switch(token.toUpperCase()){
            case "<",">","==","!=","<=",">=","LIKE" -> newComparison.addType(token);
            default -> false;
        };
    }
    public boolean isBooleanOperator(){
        return  switch(parsingToken.toUpperCase()){
            case "AND","OR" -> true;
            default -> false;
        };
    }
    public boolean isOpenBracket(){
        return parsingToken.equals("(");
    }
    public boolean isCloseBracket(){
        return parsingToken.equals(")");
    }
    public void checkValidConditions(Table table) throws GenericException {
        activeTable = table;
        parseConditions();
        validRows = new ArrayList<DataRow>();
        for(DataRow dataRow : table.DataList){
            for(String validID : comparisonsList.get(0).validIDList){
                if(dataRow.DataPoints.get(0).equals(validID)){
                    validRows.add(dataRow);
                }
            }
        }
    }
}
