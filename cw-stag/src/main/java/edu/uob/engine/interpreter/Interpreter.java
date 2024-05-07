package edu.uob.engine.interpreter;

import edu.uob.engine.GameAction;
import edu.uob.engine.entities.GameEntity;
import edu.uob.engine.entities.Location;
import edu.uob.engine.entities.Player;
import edu.uob.utilities.GenericException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Predicate;

public class Interpreter {
    ArrayList<Player> players;
    ArrayList<Location> locations;
    HashMap<String, HashSet<GameAction>> possibleActions;
    HashSet<GameAction> currentActions;
    ArrayList<GameEntity> allEntities;
    ArrayList<GameEntity> specifiedEntities;
    Player player = null;
    String playerName;

    public Interpreter(ArrayList<Player> playersInput, ArrayList<Location> locationsInput,
                       HashMap<String, HashSet<GameAction>> possibleActionsInput, ArrayList<GameEntity> entitiesInput){
        players = playersInput;
        locations = locationsInput;
        possibleActions = possibleActionsInput;
        allEntities = entitiesInput;
    }

    public String handleCommand(String command) throws GenericException {
        if(command.isEmpty()){return "";}
        command = determinePlayerName(command);
        ArrayList<String> tokens = tokenize(command);
        searchForPlayer();
        searchTriggerPhrase(tokens);
        searchEntities(tokens);
        checkConsistentEntities();
        return executeAction();
    }
    private void searchForPlayer() throws GenericException {
        for(Player playerPotential : players){
            if(playerName.equals(playerPotential.getName())){player = playerPotential;return;}
        }
        createNewPlayer();
    }
    private void searchTriggerPhrase(ArrayList<String> tokens) throws GenericException {
        currentActions = new HashSet<GameAction>();
        for(String token : tokens) {
            HashSet<GameAction> returnedSet = possibleActions.get(token);
            if (returnedSet!=null){
                currentActions.addAll(possibleActions.get(token));
            }
        }
        if(currentActions.isEmpty()){throw new GenericException("What do you do?");}
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
    }
    private void checkConsistentEntities() throws GenericException {
        Predicate<GameAction> condition = action -> !action.checkEntities(specifiedEntities,player);;
        currentActions.removeIf(condition);
        for(GameAction action : currentActions){action.checkNoEntities(specifiedEntities);}
        if(currentActions.isEmpty()){throw new GenericException("What do you do?");}
        if(currentActions.size()>1){throw new GenericException("That was ambiguous - what do you do?");}
    }
    private String executeAction( ) throws GenericException {
        return currentActions.stream().toList().get(0).execute(player,players,specifiedEntities);
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
                    if(Character.isAlphabetic(character)) {
                        builder.append(Character.toLowerCase(character));
                    }
            }
        }
        tokens.add(builder.toString());
        return tokens;
    }
}
