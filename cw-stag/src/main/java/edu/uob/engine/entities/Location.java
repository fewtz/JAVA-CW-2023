package edu.uob.engine.entities;

import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import edu.uob.utilities.GenericException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Location extends GameEntity {
    private ArrayList<Artefact> artefacts = new ArrayList<>();
    private ArrayList<Furniture> furnitures = new ArrayList<>();
    private ArrayList<Character> characters = new ArrayList<>();
    private ArrayList<Location> destinations = new ArrayList<>();
    private StringBuilder builder = new StringBuilder();
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
    public boolean remove(GameEntity item){
        if(item.getClass() == Artefact.class){
            return artefacts.remove((Artefact) item);
        }else if(item.getClass() == Furniture.class){
            return furnitures.remove((Furniture) item);
        }else if(item.getClass() == Character.class){
            return characters.remove((Character) item);
        } else if(item.getClass() == Location.class) {
            if(destinations.contains((Location)item)){
                return destinations.remove((Location)item);
            }
        }
        return false;
    }
    public boolean containsSubject(GameEntity entity){
        if(entity.getClass() == Artefact.class){
            return artefacts.contains((Artefact) entity);
        }else if(entity.getClass() == Furniture.class){
            return furnitures.contains((Furniture) entity);
        }else if(entity.getClass() == Character.class){
            return characters.contains((Character) entity);
        } else if(entity.getClass() == Location.class) {
            return destinations.contains((Location)entity);
        }
        return false;
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
