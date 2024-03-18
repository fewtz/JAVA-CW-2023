package edu.uob;

import java.util.ArrayList;

public class ComboComparison {
    Comparison firstComparison;
    Comparison secondComparison;
    ArrayList<String> newValidIds;

    enum boolType {
        AND,
        OR,
    }

    ;
    boolType comparisonType;

    ComboComparison() {
    }

    public void addBooleanType(String input) {
        if (input.equals("AND")) {
            comparisonType = boolType.AND;
        } else {
            comparisonType = boolType.OR;
        }
    }

    public void addFirstComparison(Comparison input) {
        firstComparison = input;
    }

    public void addSecondComparison(Comparison input) {
        secondComparison = input;
    }

    public Comparison evaluateCombo() {
        Comparison newComparison = new Comparison();
        newValidIds = new ArrayList<String>();
        if (comparisonType == boolType.AND) {evaluateAND();}
        if(comparisonType == boolType.OR){evaluateOR();}
        newComparison.validIDList = newValidIds;
        return newComparison;
    }
    private void evaluateOR() {
        newValidIds.addAll(firstComparison.validIDList);
        for (String ID2 : secondComparison.validIDList) {
            if(!newValidIds.contains(ID2)){
                newValidIds.add(ID2);
            }
        }
    }
    private void evaluateAND() {
        for (String ID : firstComparison.validIDList) {
            for (String ID2 : secondComparison.validIDList) {
                if (ID.equals(ID2)) {
                    newValidIds.add(ID);
                }
            }
        }
    }
}