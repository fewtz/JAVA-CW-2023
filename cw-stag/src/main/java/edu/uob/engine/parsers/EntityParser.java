package edu.uob.engine.parsers;

import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import edu.uob.engine.GameEngine;
import edu.uob.utilities.GenericException;
import edu.uob.engine.entities.GameEntity;
import edu.uob.engine.entities.Location;

import java.util.ArrayList;
import java.io.File;
public class EntityParser extends ParserFramework{
    ArrayList<GameEntity> allEntities;
    ArrayList<Location> locations;
    public EntityParser(){}
    public ArrayList<Location> parse(File entitiesFile, ArrayList<GameEntity> entities) throws GenericException {
        allEntities = entities;
        Parser parser = new Parser();
        try {
            parser.parse(createReader(entitiesFile));
        }catch(ParseException e) {
            throw new GenericException("Parsing failed: invalid entities file.");
        }
        Graph wholeDocument = parser.getGraphs().get(0);
        ArrayList<Graph> sections = wholeDocument.getSubgraphs();
        locations = parseLocations(sections.get(0).getSubgraphs());
        setupPaths(sections.get(1));
        return locations;
    }
    private ArrayList<Location> parseLocations(ArrayList<Graph> locationGraphs){
        ArrayList<Location> locationsSetup= new ArrayList<Location>();
        for (Graph locationGraph : locationGraphs) {
            Node locationDetails = locationGraph.getNodes(false).get(0);
            String name = locationDetails.getId().getId();
            String description = locationDetails.getAttribute("description");
            Location newLocation = new Location(name, description, locationGraph);
            locationsSetup.add(newLocation);
            newLocation.addSubEntities(allEntities);
        }
        return locationsSetup;
    }
    private void setupPaths(Graph pathGraph) throws GenericException {
        ArrayList<Edge> paths = pathGraph.getEdges();
        for(Edge path : paths){
            assignPath(path.getSource().getNode().getId().getId() , path.getTarget().getNode().getId().getId());
        }
    }
    private void assignPath(String origin, String destination) throws GenericException {
        Location fromLocation=null;
        Location toLocation=null;
        for(Location location : locations){
            if(origin.equals(location.getName())){
                fromLocation = location;
            }else if(destination.equals(location.getName())){
                toLocation = location;
            }
        }
        if(fromLocation==null ){ throw new GenericException("Error: unknown origin in path");}
        if(toLocation==null ){ throw new GenericException("Error: unknown destination in path");}
        fromLocation.addPath(toLocation);
    }
}
