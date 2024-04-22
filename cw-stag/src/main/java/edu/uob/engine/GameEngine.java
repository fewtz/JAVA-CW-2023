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
public class GameEngine {
    ArrayList<Location> locations;
    ArrayList<GameAction> possibleActions;
    EntityParser entityParser = new EntityParser();
    ActionParser actionParser = new ActionParser();
    ArrayList<GameEntity> allEntities = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<Player>();
    Interpreter interpreter;
    public GameEngine(File entitiesFile, File actionsFile) throws GenericException {
        locations =  entityParser.parse(entitiesFile, allEntities);
        setupStandardEntities();
        possibleActions = actionParser.parse(actionsFile, allEntities);
        setupStandardActions();
        interpreter = new Interpreter(players,locations, possibleActions, allEntities);
    }
    public String handleCommand(String command) throws GenericException {
        return interpreter.handleCommand(command);
    }
    private void setupStandardEntities(){
        allEntities.add(new Health("health","The source of your vitality",null));
    }
    private void setupStandardActions(){
        GameAction look = new GameAction(allEntities);
        look.setActionLook();
        possibleActions.add(look);
        GameAction inv = new GameAction(allEntities);
        inv.setActionInv();
        possibleActions.add(inv);
        GameAction get = new GameAction(allEntities);
        get.setActionGet();
        possibleActions.add(get);
        GameAction drop = new GameAction(allEntities);
        drop.setActionDrop();
        possibleActions.add(drop);
        GameAction goTo = new GameAction(allEntities);
        goTo.setActionGoTo();
        possibleActions.add(goTo);
    }
}