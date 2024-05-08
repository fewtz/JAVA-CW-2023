package edu.uob.engine.entities;

import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import edu.uob.utilities.GenericException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Location extends GameEntity {
    ArrayList<Artefact> artefacts = new ArrayList<>();
    ArrayList<Furniture> furnitures = new ArrayList<>();
    ArrayList<Character> characters = new ArrayList<>();
    ArrayList<Location> destinations = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    public Location(String name, String description, Graph graphInput){
        super(name,description,graphInput);
        builder.append(name+ " ");
        extractSubGraphs(originGraph.getSubgraphs());
    }
    public void addPath(Location destination){
        destinations.add(destination);
    }
    private void extractSubGraphs(ArrayList<Graph> graphs){
        for(Graph graph : graphs){
            String phrase = graph.getId().getId();

            switch (phrase) {
                case "furniture" -> {
                    extractFurniture(graph);
                    builder.append(phrase+" ");
                }
                case "artefacts" -> {
                    extractArtefacts(graph);
                    builder.append(phrase+" ");
                }
                case "characters" -> {
                    extractCharacters(graph);
                    builder.append(phrase+" ");
                }
            }
        }
    }
    private void extractCharacters(Graph charactersGraph) {
        for(Node artefactNode : charactersGraph.getNodes(false)){
            String name = artefactNode.getId().getId();
            String description = artefactNode.getAttribute("description");
            characters.add(new Character(name, description, charactersGraph));
            builder.append(name + " ");
        }
    }
    private void extractArtefacts(Graph artefactGraph) {
        for(Node artefactNode : artefactGraph.getNodes(false)){
            String name = artefactNode.getId().getId();
            String description = artefactNode.getAttribute("description");
            artefacts.add(new Artefact(name, description, artefactGraph));
            builder.append(name + " ");
        }
    }
    private void extractFurniture(Graph furnitureGraph) {
        for(Node furnitureNode : furnitureGraph.getNodes(false)){
            String name = furnitureNode.getId().getId();
            String description = furnitureNode.getAttribute("description");
            furnitures.add(new Furniture(name, description, furnitureGraph));
            builder.append(name + " ");
        }
    }
    public void addSubEntities(ArrayList<GameEntity> allEntities){
        allEntities.add(this);
        allEntities.addAll(artefacts);
        allEntities.addAll(furnitures);
        allEntities.addAll(characters);
    }
    public String getAsString(){
        return builder.toString();
    }
    public ArrayList<Location> getDestinations(){
        return destinations;
    }
    public ArrayList<Artefact> getArtefacts(){
        return artefacts;
    }
    public void addArtefact(Artefact item){
        artefacts.add(item);
    }
    public String getLocationContentsAsString(){
       StringBuilder builderForContents = new StringBuilder();
       for(Artefact artefact : artefacts){
           builderForContents.append(artefact.getName()+": "+artefact.getDescription()+"\n");
       }
       for(Furniture furniture : furnitures){
           builderForContents.append(furniture.getName()+": "+furniture.getDescription()+"\n");
       }
       for(Character character : characters){
           builderForContents.append(character.getName()+": "+character.getDescription()+"\n");
       }
       builderForContents.append("Plus routes to:\n");
       for(Location location : destinations){
           builderForContents.append(location.getName() + "\n");
       }
       return builderForContents.toString();
    }
    public boolean remove(GameEntity item) throws GenericException {
        return artefacts.remove(item)|| furnitures.remove(item)||item.getClass()== Location.class||
                characters.remove(item) || item.getName().equals("health") || removePath(item);
    }
    private boolean removePath(GameEntity item){
        if(item.getClass()== Location.class) {return false;}
        for (Location location : destinations){
            if(item.equals(location)){destinations.remove(item);return true;}
        }
        return false;
    }
    public boolean containsSubject(GameEntity entity){
        return artefacts.contains(entity)|| furnitures.contains(entity) || characters.contains(entity);
    }
    public void add(GameEntity item) throws GenericException {
        if(item.getClass() == Artefact.class){
            artefacts.add((Artefact) item);
        }else if(item.getClass() == Furniture.class){
            furnitures.add((Furniture) item);
        }else if(item.getClass() == Character.class){
            characters.add((Character) item);
        } else if(item.getClass() == Location.class) {
            if(!destinations.contains((Location)item)){
                destinations.add((Location)item);
            }
        }
    }
}
