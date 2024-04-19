package edu.uob.engine.entities;
import com.alexmerz.graphviz.objects.Graph;

import java.util.ArrayList;
public abstract class GameEntity {
    public String name;
    public String description;
    public Graph originGraph;
    public GameEntity(String name, String description, Graph graphInput) {
        this.name = name;
        this.description = description;
        this.originGraph = graphInput;
    }
    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }
}
