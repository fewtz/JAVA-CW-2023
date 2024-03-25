package edu.uob.Handlers;

import edu.uob.Handlers.SubHandlers.*;
import edu.uob.Utilities.GenericException;

import java.io.IOException;
import java.util.ArrayList;

public class CommandHandler {
    private ArrayList<String> tokens;
    public CommandHandler(){
    }

    public String handleNewCommand(String command) throws IOException, GenericException {
        Tokenizer tokenizer = new Tokenizer(command);
        tokens = tokenizer.tokenize();
        if(tokens.isEmpty()){return "ERROR: Failed to Tokenize";}
        int commandType = getCommandType(tokens.get(0));
        return switch (commandType) {
            case 1 -> {
                UseHandler useHandler = new UseHandler(tokens);
                yield useHandler.handleUse();
            }
            case 2 -> {
                CreateHandler createHandler = new CreateHandler(tokens);
                yield createHandler.handleCreate();
            }
            case 3 -> {
                DropHandler dropHandler = new DropHandler(tokens);
                yield dropHandler.handleDrop();
            }
            case 4 -> {
                AlterHandler alterHandler = new AlterHandler(tokens);
                yield alterHandler.handleAlter();
            }
            case 5 -> {
                InsertHandler insertHandler = new InsertHandler(tokens);
                yield insertHandler.handleInsert();
            }
            case 6 -> {
                SelectHandler selectHandler = new SelectHandler(tokens);
                yield selectHandler.handleSelect();
            }
            case 7 -> {
                UpdateHandler updateHandler = new UpdateHandler(tokens);
                yield updateHandler.handleUpdate();
            }
            case 8 -> {
                DeleteHandler deleteHandler = new DeleteHandler(tokens);
                yield deleteHandler.handleDelete();
            }
            case 9 -> {
                JoinHandler joinHandler = new JoinHandler(tokens);
                yield joinHandler.handleJoin();
            }
            default -> "ERROR: Unknown command";
        };
    }
    private int getCommandType(String command){
        return switch (command.toUpperCase()) {
            case "USE" -> 1;
            case "CREATE" -> 2;
            case "DROP" -> 3;
            case "ALTER" -> 4;
            case "INSERT" -> 5;
            case "SELECT" -> 6;
            case "UPDATE" -> 7;
            case "DELETE" -> 8;
            case "JOIN" -> 9;
            default -> 0;
        };
    }
}
