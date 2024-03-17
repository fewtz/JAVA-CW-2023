package edu.uob;

import java.util.ArrayList;
import java.util.Objects;

public class CommandHandler {
    private ArrayList<String> tokens;
    CommandHandler(){
    }

    public void handleNewCommand(String command){
        Tokenizer tokenizer = new Tokenizer(command);
        tokens = tokenizer.tokenize();
        if(tokens.isEmpty()){return;}
        int commandType = getCommandType(tokens.get(0));
        boolean isSuccess;
        switch (commandType){
            case 1:
                UseHandler uhandler = new UseHandler(tokens);
                isSuccess = uhandler.handleUse();
                break;
            case 2:
                CreateHandler chandler = new CreateHandler(tokens);
                isSuccess = chandler.handleCreate();
                break;
            case 3:
                DropHandler dhandler = new DropHandler(tokens);
                isSuccess = dhandler.handleDrop();
                break;
            case 4:
                AlterHandler ahandler = new AlterHandler(tokens);
                isSuccess = ahandler.handleAlter();
                break;
            case 5:
                InsertHandler ihandler = new InsertHandler(tokens);
                isSuccess = ihandler.handleInsert();
                break;
            case 6:
                SelectHandler shandler = new SelectHandler(tokens);
                isSuccess = shandler.handleSelect();
                break;
            case 10:
                ConditionHandler conditionHandler = new ConditionHandler(tokens);
                isSuccess = conditionHandler.parseConditions();
                break;
            default:
                isSuccess =false;
                System.out.println("Unknown Command");
         }
         if(!isSuccess){
             System.out.println("ERROR: Command Failed Interpretation");
         }
    }

    private int getCommandType(String command){
        return switch (command) {
            case "USE" -> 1;
            case "CREATE" -> 2;
            case "DROP" -> 3;
            case "ALTER" -> 4;
            case "INSERT" -> 5;
            case "SELECT" -> 6;
            case "UPDATE" -> 7;
            case "DELETE" -> 8;
            case "JOIN" -> 9;
            case "CONDITION" -> 10;
            default -> 0;
        };
    }
}
