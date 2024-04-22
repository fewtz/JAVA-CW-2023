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

    public String handleCommand(String command) throws GenericException {
        if(command.length()==0){return "";}
        command = determinePlayerName(command);
        ArrayList<String> tokens = tokenize(command);
        searchForPlayer();
        searchTriggerPhrase(tokens);
        searchEntities(tokens);
        checkConsistentEntities();
        return executeAction();
    }
    private String executeAction( ) throws GenericException {
        return currentActions.get(0).execute(player,players,specifiedEntities);
    }
    private void checkConsistentEntities() throws GenericException {
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
        //if(specifiedEntities.size()==0){throw new GenericException("Error: no entities specified in input phrase");}
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
        for(Player playerPotential : players){
            if(playerName.equals(playerPotential.getName())){player = playerPotential;return;}
        }
        createNewPlayer();
    }

    private void createNewPlayer(){
        player = new Player(playerName,"You",null,locations.get(0));
        players.add(player);
    }

    private String determinePlayerName(String command) throws GenericException {
        int charCounter = 0;
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<command.length();i++){
            char character = command.charAt(i);
            if(character == ':'){
                playerName = builder.toString();
                break;
            }else{
                builder.append(character);
                charCounter ++;
            }
        }
        if (playerName==null){throw new GenericException("Error: Player not specified correctly");}
        return command.substring(++charCounter);
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
                    if(!builder.isEmpty())tokens.add(builder.toString());
                    builder = new StringBuilder();
                    break;
                default :
                    builder.append(character);
            }
        }
        tokens.add(builder.toString());
        return tokens;
    }
}
