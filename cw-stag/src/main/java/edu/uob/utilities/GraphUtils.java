package edu.uob.utilities;

import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import edu.uob.engine.GameEngine;
import edu.uob.engine.entities.GameEntity;
import edu.uob.engine.entities.Location;

import java.util.ArrayList;
 public abstract class GraphUtils {
    public static ArrayList<GameEntity> extractGraph(Graph graph) throws GenericException {
        ArrayList<GameEntity> entities = new ArrayList<>();
        for (Node node : graph.getNodes(false)) {
            //*to do ! make this work

            String name = node.getId().getId();
            String description = node.getAttribute("description");
            //entities.add(new GameEntity<>(name, description, graph));
        }
        return entities;
    }
}
