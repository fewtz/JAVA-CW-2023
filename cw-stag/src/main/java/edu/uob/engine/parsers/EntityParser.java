package edu.uob.engine.parsers;

import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import edu.uob.utilities.GenericException;
import edu.uob.engine.entities.GameEntity;
import edu.uob.engine.entities.Location;

import java.util.ArrayList;
import java.io.File;
public class EntityParser extends ParserFramework{

    public EntityParser(){};
    ArrayList<Location> locations;

    public ArrayList<Location> parse(File entitiesFile) throws GenericException {
        Parser parser = new Parser();
        try {
            parser.parse(createReader(entitiesFile));
        }catch(ParseException e) {
            throw new GenericException("Parsing failed: invalid entities file.");
        }
        Graph wholeDocument = parser.getGraphs().get(0);
        ArrayList<Graph> sections = wholeDocument.getSubgraphs();
        locations = parseLocations(sections.get(0));
        setupPaths(sections.get(1));
        return locations;
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
    private ArrayList<Location> parseLocations(Graph locationGraph){
        ArrayList<Location> locationsSetup= new ArrayList<Location>();
        ArrayList<Node> nodes = locationGraph.getNodes(false);
        for (Node locationNode : nodes) {
            String name = locationNode.getId().getId();
            String description = locationNode.getAttribute("description");
            locationsSetup.add(new Location(name, description, locationGraph));
        }
        return locationsSetup;
    }

}
