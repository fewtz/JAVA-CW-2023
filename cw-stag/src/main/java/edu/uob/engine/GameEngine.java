package edu.uob.engine;

import edu.uob.engine.entities.Health;
import edu.uob.engine.entities.Location;
import edu.uob.engine.entities.Player;
import edu.uob.engine.interpreter.Interpreter;
import edu.uob.utilities.GenericException;
import edu.uob.engine.entities.GameEntity;
import edu.uob.engine.parsers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameEngine {
    ArrayList<Location> locations;
    HashMap<String, HashSet<GameAction>> possibleActions;
    EntityParser entityParser = new EntityParser();
    ActionParser actionParser;
    ArrayList<GameEntity> allEntities = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<Player>();
    Interpreter interpreter;
    public GameEngine(File entitiesFile, File actionsFile) throws GenericException {
        locations =  entityParser.parse(entitiesFile, allEntities);
        Location storeRoom = locations.get(locations.size()-1);
        setupStandardEntities();
        actionParser = new ActionParser(storeRoom,locations);
        possibleActions = actionParser.parse(actionsFile, allEntities);
        setupStandardActions(storeRoom);
        interpreter = new Interpreter(players,locations, possibleActions, allEntities);
    }
    public String handleCommand(String command) throws GenericException {
        return interpreter.handleCommand(command);
    }
    private void setupStandardEntities(){
        allEntities.add(new Health("health","The source of your vitality",null));
    }
    private void setupStandardActions(Location storeRoom){
        GameAction look = new GameAction(allEntities,storeRoom,locations,possibleActions);
        look.setActionLook();
        GameAction inv = new GameAction(allEntities,storeRoom,locations,possibleActions);
        inv.setActionInv();
        GameAction get = new GameAction(allEntities,storeRoom,locations,possibleActions);
        get.setActionGet();
        GameAction drop = new GameAction(allEntities,storeRoom,locations,possibleActions);
        drop.setActionDrop();
        GameAction goTo = new GameAction(allEntities,storeRoom,locations,possibleActions);
        goTo.setActionGoTo();
        GameAction health = new GameAction(allEntities,storeRoom,locations,possibleActions);
        health.setActionHealth();
    }
}
