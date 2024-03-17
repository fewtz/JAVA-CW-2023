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
        boolean testing,testing2,testing3;
        testing =parseComboCondition(currentComponent);
        testing2 = parseOpenBracket(currentComponent);
        testing3 = parseExpression(currentComponent);

        if(!(testing||testing2||testing3)){return false;}
        return furthestComponent+1==numberOfCompoents && inUseBrackets==0;
    }
    private boolean parseOpenBracket(int currentComponent){
        currentComponent = IncrementConditionList(currentComponent);
        if(currentComponent<0){return false;}
        if(activeComponent!=ConditionComponent.OPENBRACKET){return false;}
        currentComponent = IncrementConditionList(currentComponent);
        if(currentComponent<0){return false;}
        if(parseOpenBracket(currentComponent-1)){activeComponent==ConditionComponent.CLOSEBRACKET;}
        if(!(parseComboCondition(currentComponent-1)||parseExpression(currentComponent-1))){return false;}
        currentComponent = IncrementConditionList(currentComponent);
        if(currentComponent<0){return false;}
        return activeComponent==ConditionComponent.CLOSEBRACKET;
    }
    private boolean parseComboCondition(int currentComponent){
        currentComponent = IncrementConditionList(currentComponent);
        if(currentComponent<0){return false;}
        if(parseOpenBracket(currentComponent-1)){return true;}
        if(!(parseExpression(currentComponent-1))){return false;}
        System.out.println("yes " + activeComponent);
        currentComponent = IncrementConditionList(currentComponent);
        if(currentComponent<0){return false;}
        if(activeComponent!=ConditionComponent.BOOLEANOPERATOR){return false;}
        System.out.println("yes " + activeComponent);
        currentComponent = IncrementConditionList(currentComponent);
        if(currentComponent<0){return false;}
        return parseExpression(currentComponent-1);
    }
    private boolean parseExpression(int currentComponent){
        IncrementConditionList(currentComponent);
        return activeComponent==ConditionComponent.EXPRESSION;
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
