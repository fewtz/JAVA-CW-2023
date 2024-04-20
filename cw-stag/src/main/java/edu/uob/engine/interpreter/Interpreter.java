package edu.uob.engine.interpreter;

import edu.uob.engine.GameAction;
import edu.uob.engine.entities.GameEntity;
import edu.uob.engine.entities.Location;
import edu.uob.engine.entities.Player;
import edu.uob.utilities.GenericException;

import java.util.ArrayList;

public class Interpreter {
    ArrayList<Player> players;
    ArrayList<Location> locations;
    ArrayList<GameAction> possibleActions;
    ArrayList<GameAction> currentActions;
    ArrayList<GameEntity> allEntities;
    ArrayList<GameEntity> specifiedEntities;

    Player player = null;
    String playerName;

    public Interpreter(ArrayList<Player> playersInput, ArrayList<Location> locationsInput,
                       ArrayList<GameAction> possibleActionsInput, ArrayList<GameEntity> entitiesInput){
        players = playersInput;
        locations = locationsInput;
        possibleActions = possibleActionsInput;
        allEntities = entitiesInput;
    }

    public Object handleCommand(String command) throws GenericException {
        if(command.length()==0){return "";}
        ArrayList<String> tokens = tokenize(command);
        determinePlayerName(tokens);
        searchForPlayer();
        searchTriggerPhrase(tokens);
        searchEntities(tokens);
        checkConsistentEntities();
        return executeAction();
    }
    private String executeAction( ) throws GenericException {
        return currentActions.get(0).execute(player,specifiedEntities);
    }
    private void checkConsistentEntities(){
        currentActions.get(0).checkEntities(specifiedEntities);
    }
    private void searchEntities(ArrayList<String> tokens) throws GenericException {
        specifiedEntities = new ArrayList<>();
        for(String token : tokens){
            for(GameEntity entity : allEntities){
                if(token.equalsIgnoreCase(entity.getName())){
                    specifiedEntities.add(entity);
                }
            }
        }
        if(specifiedEntities.size()==0){throw new GenericException("Error: no entities specified in input phrase");}
    }
    private void searchTriggerPhrase(ArrayList<String> tokens) throws GenericException {
        currentActions = new ArrayList<>();
        for(String token : tokens){
            for(GameAction possibleAction : possibleActions){
                if(possibleAction.isTrigger(token)){
                    currentActions.add(possibleAction);
                }
            }
        }
        if(currentActions.size()==0){throw new GenericException("Error: no trigger word in input phrase");}
        if(currentActions.size()>1){throw new GenericException("Error: more than one trigger word in input phrase");}
    }
    private void searchForPlayer() throws GenericException {
        if(players.size()==0) {throw new GenericException("Error: no players found on record");}
        for(Player playerPotential : players){
            if(playerName.equals(playerPotential.getName())){player = playerPotential;}
        }
        if(player == null) {throw new GenericException("Error: player not found");}
    }

    private void determinePlayerName(ArrayList<String> tokens) throws GenericException {
        StringBuilder builder = new StringBuilder();
        for(String token : tokens){
            if(token.equals(":")){
                tokens.remove(token);
                playerName = builder.toString();
                break;
            }else{
                builder.append(token);
                tokens.remove(token);
            }
        }
        if (playerName==null){throw new GenericException("Error: Player not specified correctly");}
    }
    private ArrayList<String> tokenize(String command){
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i< command.length() ; i++){
            char character = command.charAt(i);
            switch(character){
                case ':' :
                    tokens.add(builder.toString());
                    tokens.add(":");
                    builder = new StringBuilder();
                    break;
                case ' ' :
                    tokens.add(builder.toString());
                    builder = new StringBuilder();
                    break;
                default :
                    builder.append(character);
            }
        }
        return tokens;
    }
}
