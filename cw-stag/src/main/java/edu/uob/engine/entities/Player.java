package edu.uob.engine.entities;

import com.alexmerz.graphviz.objects.Graph;
import edu.uob.utilities.GenericException;

import java.util.ArrayList;

public class Player extends GameEntity{
    ArrayList<Artefact> inventory = new ArrayList<>();
    int health = 3;
    Location currentLocation; // what should this be initalized to ?
    public Player(String name, String description, Graph graphInput, Location locationInput) {
        super(name, description, graphInput);
        currentLocation = locationInput;
    }
    public Location getLocation(){
        return currentLocation;
    }
    public void setLocation(Location location){
        currentLocation = location;
    }
    public void add(GameEntity item) throws GenericException {
        if(item.getName().equals("health")){
            if(health>=3){
                throw new GenericException("You are already as healthy as can be");
            }
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
    public boolean remove(GameEntity item) throws GenericException {
        if(item.getName().equals("health")){
            health--;
            return true;
        }else{
            return inventory.remove(item);
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
