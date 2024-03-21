package edu.uob;

import java.util.ArrayList;

public class AlterHandler extends Handler{
    private int AlterationType;
    AlterHandler(ArrayList<String> Input){
        tokens = Input;
    }
    public String handleAlter() throws GenericException {
        CurrentToken=0;
        IncrementToken();
        if(!ActiveToken.equals("TABLE")){throw new GenericException("[ERROR] : Only table alters are permitted") ;}
        IncrementToken();
        if((activeTable = isTable(activeTable))==null){throw new GenericException("[ERROR] : Table not valid");}
        IncrementToken();
        if((AlterationType=whatAlteration())==0){throw new GenericException("[ERROR] : Invalid alteration type");}
        IncrementToken();
        if(!alterAttribute()){return "[ERROR] : Unable to alter table";}
        IncrementToken();
        if(!ActiveToken.equals(";")){return "[ERROR] : Missing or misplaced ';'";}
        return "[OK] \nTable Altered successfully \n" + activeTable.getTableAsString();
    }
    private boolean alterAttribute(){
        switch(AlterationType) {
            case 1:
                for (String title : activeTable.columnNames) {
                    if (ActiveToken.equals(title)) {
                        return false;
                    }
                }
                activeTable.addColumn(ActiveToken);
                return true;
            case 2:
                for (String title : activeTable.columnNames) {
                    if (ActiveToken.equals(title)) {
                        activeTable.removeColumn(title);
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }
    private int whatAlteration(){
        return switch (ActiveToken) {
            case "ADD" -> 1;
            case "DROP" -> 2;
            default -> 0;
        };
    }
}
