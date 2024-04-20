package edu.uob.engine.entities;

import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Location extends GameEntity {
    ArrayList<Artefact> artefacts = new ArrayList<>();;
    ArrayList<Furniture> furnitures = new ArrayList<>();;
    ArrayList<Character> characters = new ArrayList<>();;
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
}
