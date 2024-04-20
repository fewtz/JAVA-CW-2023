package edu.uob.engine.entities;

import com.alexmerz.graphviz.objects.Graph;
import edu.uob.utilities.GenericException;

import java.util.ArrayList;

public class Player extends GameEntity{
    ArrayList<GameEntity> inventory = new ArrayList<>();
    int health = 3;
    public Player(String name, String description, Graph graphInput) {
        super(name, description, graphInput);
    }

    public void add(GameEntity item){
        if(item.getName().equals("health")){
            health++;
        }else{
            inventory.add(item);
        }
    }
    public void remove(GameEntity item) throws GenericException {
        if(item.getName().equals("health")){
            health--;
        }else{
            if(!inventory.remove(item)){
                throw new GenericException("Error: tried to remove an item which the player didnt have");
            }
        }
    }
}
