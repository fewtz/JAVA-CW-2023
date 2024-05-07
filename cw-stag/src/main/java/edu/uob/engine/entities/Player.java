package edu.uob.engine.entities;

import com.alexmerz.graphviz.objects.Graph;
import edu.uob.utilities.GenericException;

import java.util.ArrayList;

public class Player extends GameEntity{
    ArrayList<Artefact> inventory = new ArrayList<>();
    int health = 3;
    Location currentLocation; // what should this be initalized to ?
    Location startLocation;
    public Player(String name, String description, Graph graphInput, Location locationInput) {
        super(name, description, graphInput);
        currentLocation = locationInput;
        startLocation = locationInput;
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
            if(health<=0){die();}
            return true;
        }else{
            return inventory.remove(item);
        }
    }
    private void die() throws GenericException {
        for(int i=inventory.size()-1 ;i>=0 ;i--){
            Artefact item = inventory.get(i);
            inventory.remove(item);
            currentLocation.addArtefact(item);
        }
        health = 3;
        currentLocation = startLocation;
        throw new GenericException("You have died. All of your items have been dropped,"
                +"and you have been returned to the start of the game.");
    }
    public void getItem(GameEntity item) throws GenericException {
        for(Artefact artefact : currentLocation.getArtefacts()){
            if(artefact.equals(item)){
                this.add(artefact);
                return;
            }
        }
        throw new GenericException("That item does not exist in location for you to get");
    }
    public void dropItem(GameEntity item) throws GenericException{
        for(int i=inventory.size()-1;i>=0; i--){
            Artefact artefact = inventory.get(i);
            if (artefact.equals(item)){
                remove(item);
                currentLocation.addArtefact(artefact);
                return;
            }
        }
        throw new GenericException("That item does not exist in your inventory to drop");
    }
    public String getInvAsString(){
        StringBuilder builder = new StringBuilder();
        for(Artefact item : inventory){
            builder.append(item.getName()+"\n");
        }
        return builder.toString();
    }
    public String getHealth(){return Integer.toString(health);}
    public boolean containsSubject(GameEntity entity){
        return inventory.contains(entity);
    }
}
