package edu.uob;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SelectHandler extends Handler{
    Table activeTable;
    ArrayList<String> attributeList;
    SelectHandler(ArrayList<String> Input) {
        tokens = Input;
    }
    public boolean handleSelect(){
        CurrentToken =0;
        IncrementToken();
        if(!isAttributeList()){return false;}
        IncrementToken();
        if(!ActiveToken.equals("FROM")){return false;}
        IncrementToken();
        if(!isTable()){return false;}
        IncrementToken();
        if(ActiveToken.equals(";")){return true;}
        if(!ActiveToken.equals("WHERE")){return false;}
        IncrementToken();
        if(!validConditions()){return false;}
        IncrementToken();
        return ActiveToken.equals(";");
    }
    private boolean isAttributeList(){
        IncrementToken();
        if(!isPlainText()){return false;}
        attributeList.add(ActiveToken);
        IncrementToken();
        if(ActiveToken.equals(",")){return isAttributeList();}
        return ActiveToken.equals(")");
    }
    public boolean validConditions(){

        return true;
    }
}
