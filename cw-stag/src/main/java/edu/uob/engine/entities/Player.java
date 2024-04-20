package edu.uob.engine.entities;

import com.alexmerz.graphviz.objects.Graph;
import edu.uob.utilities.GenericException;

import java.util.ArrayList;

public class Player extends GameEntity{
    ArrayList<Artefact> inventory = new ArrayList<>();
    int health = 3;
    Location currentLocation; // what should this be initalized to ?
    public Player(String name, String description, Graph graphInput) {
        super(name, description, graphInput);
    }
    public Location getLocation(){
        return currentLocation;
    }
    public void setLocation(Location location){
        currentLocation = location;
    }
    public void add(GameEntity item){
        if(item.getName().equals("health")){
            health++;
        }else{
            if(item.getClass()== Artefact.class){
                inventory.add((Artefact) item);
            }
            if(item.getClass()== Location.class){
                currentLocation.addPath((Location)item);
            }
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
    public void getItem(GameEntity item) throws GenericException {
        for(Artefact artefact : currentLocation.getArtefacts()){
            if(artefact.equals(item)){
                this.add(artefact);
                return;
            }
        }
        throw new GenericException("Error: item does not exist in location for get");
    }
    public void dropItem(GameEntity item) throws GenericException{
        for(Artefact artefact : inventory){
            if (artefact.equals(item)){
                remove(item);
                currentLocation.addArtefact(artefact);
            }
        }
        throw new GenericException("Error: item does not exist in inventory");
    }
    public String getInvAsString(){
        StringBuilder builder = new StringBuilder();
        for(Artefact item : inventory){
            builder.append(item.getName()+"\n");
        }
        return builder.toString();
    }
}
