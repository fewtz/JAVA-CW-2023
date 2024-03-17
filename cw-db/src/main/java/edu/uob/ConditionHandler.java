package edu.uob;

import java.beans.Expression;
import java.util.ArrayList;

public class ConditionHandler extends Handler{
    enum ConditionComponent{
        BOOLEANOPERATOR,
        EXPRESSION,
        OPENBRACKET,
        CLOSEBRACKET,
    };
    String parsingToken;
    int ReturnVal;
    ArrayList<ConditionComponent> conditionList;
    int placeInConditionList=0;
    int currentParsingToken;
    ConditionComponent activeComponent;
    int inUseBrackets = 0;
    int numberOfCompoents;
    int furthestComponent=0;
    ConditionHandler(ArrayList<String> Input){
        tokens=Input;
    }
    public boolean parseConditions(){
        conditionList = new ArrayList<ConditionComponent>();
        ActiveToken = tokens.get(1);
        parsingToken = ActiveToken;
        currentParsingToken=2;
        while(!parsingToken.equals(";")){
            checkConditionComponentType();
            IncrementParsingToken();
        }
        printConditionList();
        numberOfCompoents=conditionList.size();

        return parseCondition(0);
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

    private boolean parseCondition(int currentComponent){
        int testing,testing2,testing3;
        testing = parseComboCondition(currentComponent);
        testing2 = parseOpenBracket(currentComponent);
        testing3 = parseExpression(currentComponent);
        if(testing<0 && testing2<0 && testing3<0){return false;}
        return furthestComponent+1>=numberOfCompoents && inUseBrackets==0;
    }
    private int parseOpenBracket(int currentComponent){
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if(activeComponent!=ConditionComponent.OPENBRACKET){return -1;}
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if((ReturnVal = parseComboCondition(currentComponent-1))>0){
            currentComponent = ReturnVal;
        }else{
            if((ReturnVal = parseOpenBracket(currentComponent-1))>0){
                currentComponent = ReturnVal;
            }else{
                if((ReturnVal = parseExpression(currentComponent-1))>0){
                    currentComponent = ReturnVal;
                }else{
                    return -1;
                }
            }
        }
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if(activeComponent!=ConditionComponent.CLOSEBRACKET){return -1;}
        else{return currentComponent;}
    }
    private int parseComboCondition(int currentComponent){
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if((ReturnVal = parseOpenBracket(currentComponent-1))>0){currentComponent = ReturnVal;}
        else if((ReturnVal = parseExpression(currentComponent-1))>0){currentComponent = ReturnVal;}
        else{return -1;}
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if(activeComponent!=ConditionComponent.BOOLEANOPERATOR){return -1;}
        if((currentComponent = IncrementConditionList(currentComponent))<0){return -1;}
        if((ReturnVal = parseOpenBracket(currentComponent-1))>0){currentComponent = ReturnVal;}
        else if((ReturnVal = parseExpression(currentComponent-1))>0){currentComponent = ReturnVal;}
        else{return -1;}
        return ReturnVal;
    }
    private int parseExpression(int currentComponent){
        IncrementConditionList(currentComponent);
        if(activeComponent!=ConditionComponent.EXPRESSION){return -1;}
        else{return currentComponent+1;}
    }
    public int IncrementConditionList(int currentComponent){
        if(currentComponent>=furthestComponent){furthestComponent=currentComponent;}
        if(currentComponent>=numberOfCompoents){return -1;}
        activeComponent = conditionList.get(currentComponent);
        return currentComponent+1;
    }
    public void IncrementParsingToken(){
        parsingToken=tokens.get(currentParsingToken++);
    }
    public void checkConditionComponentType(){
        //I'm sure this could be improved with a stream, but i haven't been taught that yet
        if(isBooleanOperator()){conditionList.add(ConditionComponent.BOOLEANOPERATOR);}
        if(isOpenBracket()){conditionList.add(ConditionComponent.OPENBRACKET);}
        if(isCloseBracket()){conditionList.add(ConditionComponent.CLOSEBRACKET);}
        if(isExpression()){conditionList.add(ConditionComponent.EXPRESSION);}
    }
    public boolean isExpression(){
        return switch(parsingToken){
            case "<", "LIKE", ">", "==", "<=", ">=", "!=" -> true;
            default -> false;
        };
    }
    public boolean isBooleanOperator(){
        return  switch(parsingToken){
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
}
