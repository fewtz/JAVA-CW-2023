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

    private void setupStandardEntities(){
        allEntities.add(new Health("health","The source of your vitality",null));
    }

    private void setupStandardActions(){
        GameAction look = new GameAction(allEntities);
        look.setActionLook();
        possibleActions.add(new GameAction(allEntities));
        GameAction inv = new GameAction(allEntities);
        inv.setActionInv();
        possibleActions.add(new GameAction(allEntities));
        GameAction get = new GameAction(allEntities);
        get.setActionGet();
        possibleActions.add(new GameAction(allEntities));
        GameAction drop = new GameAction(allEntities);
        drop.setActionDrop();
        possibleActions.add(new GameAction(allEntities));
        GameAction goTo = new GameAction(allEntities);
        goTo.setActionGoTo();
        possibleActions.add(new GameAction(allEntities));
    }
}
