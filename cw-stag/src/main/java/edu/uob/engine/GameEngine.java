package edu.uob.engine;

import edu.uob.engine.entities.Location;
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
    public GameEngine(File entitiesFile, File actionsFile) throws GenericException {
        locations =  entityParser.parse(entitiesFile, allEntities);
        possibleActions = actionParser.parse(actionsFile, allEntities);
        for(Location location : locations){
            System.out.print(location.getAsString()+"\n");
        }
        for(GameAction action : possibleActions){
            System.out.print(action.getAsString()+"\n");
        }
    }
}
