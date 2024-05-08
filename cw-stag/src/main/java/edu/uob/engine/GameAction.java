package edu.uob.engine;
import edu.uob.engine.entities.Artefact;
import edu.uob.engine.entities.GameEntity;
import edu.uob.engine.entities.Location;
import edu.uob.engine.entities.Player;
import edu.uob.utilities.GenericException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameAction{
    private ArrayList<GameEntity> allEntities;
    private ArrayList<String> triggers = new ArrayList<>();
    private ArrayList<GameEntity> subjects = new ArrayList<>();
    private ArrayList<GameEntity> consumed= new ArrayList<>();
    private ArrayList<GameEntity> produced= new ArrayList<>();
    private String narration;
    private ActionType actionType=ActionType.nonstandard;
    private StringBuilder builder = new StringBuilder();
    private Location storeRoom;
    private ArrayList<Location> locations;
    private HashMap<String,HashSet<GameAction>> actionList;

    public GameAction(ArrayList<GameEntity> entitiesInput, Location storeRoomInput,
                      ArrayList<Location> locationsInput, HashMap<String, HashSet<GameAction>> actionListInput ){
        allEntities = entitiesInput;
        storeRoom = storeRoomInput;
        locations = locationsInput;
        actionList = actionListInput;
    }
    public void setTriggers(Element input) throws GenericException {
        NodeList phrases = input.getElementsByTagName("keyphrase");
        builder.append("triggers: ");
        for(int i=0; i< phrases.getLength() ; i++ ){
            String triggerPhrase = phrases.item(i).getTextContent();
            if(triggerPhrase!=null){
                triggers.add(triggerPhrase);
                builder.append(triggerPhrase + " ");
                addToMap(triggerPhrase);
            }
        }
    }
    private void addToMap(String triggerPhrase) {
        if (actionList.containsKey(triggerPhrase)) {
            actionList.get(triggerPhrase).add(this);
        } else {
            HashSet<GameAction> newActionSet = new HashSet<>();
            newActionSet.add(this);
            actionList.put(triggerPhrase, newActionSet);
        }
    }
    public void setSubjects(Element input) throws GenericException {
        NodeList subjectsNode = input.getElementsByTagName("entity");
        builder.append("subject: ");
        extractEntity(input,subjects);
    }
    public void setConsumed(Element input) throws GenericException {
        builder.append("consumed: ");
        extractEntity(input,consumed);
    }
    public void setProduced(Element input) throws GenericException {
        builder.append("produced: ");
        extractEntity(input,produced);
    }
    private void extractEntity(Element input, ArrayList<GameEntity> outputList){
        NodeList phrases = input.getElementsByTagName("entity");
        for(int i=0; i< phrases.getLength() ; i++ ) {
            String entityName = phrases.item(i).getTextContent();
            GameEntity entity = entitySearch(entityName);
            if (entity != null) {
                outputList.add(entity);
                builder.append(entityName + " ");
            }
        }
    }
    public void setNarration(Element input){
        builder.append("narration: ");
        narration = input.getTextContent();
        builder.append(narration);
    }
    private GameEntity entitySearch(String input){
        for(GameEntity entity : allEntities){
            if(entity.getName().equals(input)){return entity;}
        }
        return null;
    }
    public void checkNoEntities(ArrayList<GameEntity> entities) throws GenericException {
        if(actionType==ActionType.nonstandard && entities.isEmpty()){
            throw new GenericException("What would you like to do that with?");
        }
    }
    public boolean checkEntities(ArrayList<GameEntity> entities,Player player){
        if(actionType!=ActionType.nonstandard){return true;}
        for(GameEntity entity : entities){
            boolean hasAppeared=false;
            for(GameEntity subject : subjects) {
                if (entity.equals(subject)) {
                    hasAppeared = true;
                }
            }
            if (!hasAppeared) {return false;}
        }
        for(GameEntity subject : subjects){
            boolean isHere = player.containsSubject(subject) || player.getLocation().containsSubject(subject);
            if(!isHere){return false;}
        }
        return true;
    }
    public void setActionLook(){
        actionType = ActionType.look;
        triggers.add("look");
        addToMap("look");
    }
    public void setActionGet(){
        actionType = ActionType.get;
        triggers.add("get");
        addToMap("get");
    }
    public void setActionDrop(){
        actionType = ActionType.drop;
        triggers.add("drop");
        addToMap("drop");
    }
    public void setActionInv(){
        actionType = ActionType.inv;
        triggers.add("inv");
        addToMap("inv");
        triggers.add("inventory");
        addToMap("inventory");
    }
    public void setActionGoTo(){
        actionType = ActionType.goTo;
        triggers.add("goto");
        addToMap("goto");
    }
    public void setActionHealth(){
        actionType=ActionType.health;
        triggers.add("health");
        addToMap("health");
    }
    public void setActionTypeNonstandard(){
        actionType = ActionType.nonstandard;
    }

    public String execute(Player player,ArrayList<Player> players, ArrayList<GameEntity> specifiedEntities) throws GenericException {
        switch(actionType){
            case nonstandard:
                for(GameEntity consumedItem : consumed) {
                    consumeItem(player,consumedItem);
                }
                for(GameEntity producedItem : produced) {
                    produceItem(player, producedItem);
                }
                break;
            case goTo:
                processGoTo(player,specifiedEntities);
                break;
            case get:
                processGet(player,specifiedEntities);
                break;
            case drop:
                processDrop(player,specifiedEntities);
                break;
            case inv:
                if(!specifiedEntities.isEmpty()){throw new GenericException("What do you do?");}
                processInv(player);
                break;
            case look:
                if(!specifiedEntities.isEmpty()){throw new GenericException("What do you do?");}
                processLook(player,players);
                break;
            case health:
                if(specifiedEntities.size()!=1){throw new GenericException("What do you do?");}
                processHealth(player);
                break;
            default:
                break;
        }
        return narration;
    }
    private void produceItem(Player player, GameEntity producedItem) throws GenericException {
        boolean hasBeenFound = false;
        if(producedItem.getName().equals("health")){player.add(producedItem);return;}
        for(Location location : locations){
            if(producedItem.getClass()== Location.class){hasBeenFound=true;break;}
            if(location.containsSubject(producedItem)){
                hasBeenFound = location.remove(producedItem);
                break;
            }
        }
        if(!hasBeenFound){throw new GenericException("The produced item could not be found, how peculiar..");}
        player.getLocation().add(producedItem);
    }
    private void consumeItem(Player player, GameEntity consumedItem) throws GenericException {
        boolean isRemoved = false;
        if(consumedItem.getName().equals("health")){player.remove(consumedItem);return;}
        if(consumedItem.getClass()== Location.class){
            if(!player.getLocation().remove(consumedItem)){throw new GenericException("What do you do?");}
            return;
        }
        for(Location location : locations){
            if(location.containsSubject(consumedItem)){
                isRemoved = isRemoved ||location.remove(consumedItem);
                break;
            }
            isRemoved = isRemoved || player.remove(consumedItem);
        }
        if(!isRemoved){throw new GenericException("A consumption item does not exist");}
        storeRoom.add(consumedItem);
    }
    private void processGoTo(Player player ,ArrayList<GameEntity> specifiedEntities) throws GenericException {
       if(specifiedEntities.size()!=1){throw new GenericException("You may only go to one place at a time");}
       for(Location location : player.getLocation().getDestinations()){
           if(location.equals(specifiedEntities.get(0))){
               player.setLocation(location);
               narration = "You are now in " + location.getName();
               return;
           }
       }
       throw new GenericException("You cannot get there from here");
    }
    private void processGet(Player player ,ArrayList<GameEntity> specifiedEntities)throws GenericException{
        if(specifiedEntities.size()!=1){throw new GenericException("You may only get one thing at a time");}
        player.getItem(specifiedEntities.get(0));
        player.getLocation().remove(specifiedEntities.get(0));
        narration = "You picked up the " + specifiedEntities.get(0).getName();
    }
    private void processDrop(Player player ,ArrayList<GameEntity> specifiedEntities) throws GenericException {
        if(specifiedEntities.size()!=1){throw new GenericException("You may only drop one thing at a time");}
        player.dropItem(specifiedEntities.get(0));
        narration = "You dropped up the " + specifiedEntities.get(0).getName();
    }
    private void processInv(Player player){
        narration = "The items in your inventory are as follows:\n" + player.getInvAsString();
    }
    private void processHealth(Player player){
        narration = "Current health:" + player.getHealth();
    }
    private ArrayList<Player> getOtherPlayersHere(Player player,ArrayList<Player> players){
        ArrayList<Player> others = new ArrayList<>();
        for(Player other : players){
            if(!other.equals(player) && other.getLocation().equals(player.getLocation())){
                others.add(other);
            }
        }
        return others;
    }
    private void processLook(Player player,ArrayList<Player> players){
        narration = "You look around "+player.getLocation().getDescription()
                +" and see:\n"+ player.getLocation().getLocationContentsAsString();
        ArrayList<Player> othersHere = getOtherPlayersHere(player,players);
        if(othersHere.size()==1){
            narration+= "You also see a human named:\n"+othersHere.get(0).getName()+"\n";
        }
        if(othersHere.size()>1){
            narration+= "You also see a number of other humans named:\n";
            for(Player otherPlayer : othersHere){
                narration+=otherPlayer.getName()+"\n";
            }
        }
    }
}
enum ActionType {
    look,
    get,
    drop,
    inv,
    goTo,
    health,
    nonstandard
}