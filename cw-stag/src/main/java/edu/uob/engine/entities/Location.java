package edu.uob.engine.entities;

import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Location extends GameEntity {
    ArrayList<Artefact> artefacts;
    ArrayList<Furniture> furnitures;
    ArrayList<Character> characters;
    ArrayList<Location> destinations;
    public Location(String name, String description, Graph graphInput){
        super(name,description,graphInput);
        extractSubGraphs(originGraph.getSubgraphs());
    }
    public void addPath(Location destination){
        destinations.add(destination);
    }
    private void extractSubGraphs(ArrayList<Graph> graphs){
        for(Graph graph : graphs){
            switch (graph.getId().getId()) {
                case "furniture" -> {
                    extractArtefacts(graph);
                }
                case "artefacts" -> {
                    extractFurniture(graph);
                }
                case "characters" -> {
                    extractCharacters(graph);
                }
            }
        }
    }
    private void extractCharacters(Graph charactersGraph) {
        characters = new ArrayList<>();
        for(Node artefactNode : charactersGraph.getNodes(false)){
            String name = artefactNode.getId().getId();
            String description = artefactNode.getAttribute("description");
            characters.add(new Character(name, description, charactersGraph));
        }
    }
    private void extractArtefacts(Graph artefactGraph) {
        artefacts = new ArrayList<>();
        for(Node artefactNode : artefactGraph.getNodes(false)){
            String name = artefactNode.getId().getId();
            String description = artefactNode.getAttribute("description");
            artefacts.add(new Artefact(name, description, artefactGraph));
        }
    }
    private void extractFurniture(Graph furnitureGraph) {
        furnitures = new ArrayList<>();
        for(Node furnitureNode : furnitureGraph.getNodes(false)){
            String name = furnitureNode.getId().getId();
            String description = furnitureNode.getAttribute("description");
            furnitures.add(new Furniture(name, description, furnitureGraph));
        }
    }
}
