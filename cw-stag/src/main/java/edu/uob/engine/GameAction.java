package edu.uob.engine;
import edu.uob.engine.entities.Artefact;
import edu.uob.engine.entities.GameEntity;
import edu.uob.engine.entities.Location;
import edu.uob.engine.entities.Player;
import edu.uob.utilities.GenericException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
public class GameAction{
    private ArrayList<GameEntity> allEntities;
    private ArrayList<String> triggers = new ArrayList<>();
    private ArrayList<GameEntity> subjects = new ArrayList<>();
    private GameEntity consumed=null;
    private GameEntity produced=null;
    private String narration;
    private ActionType actionType=ActionType.nonstandard;
    private StringBuilder builder= new StringBuilder();

    public GameAction(ArrayList<GameEntity> entitiesInput){
        allEntities = entitiesInput;
    }

    public void setTriggers(Element input) throws GenericException {
        NodeList phrases = input.getElementsByTagName("keyphrase");
        builder.append("triggers: ");
        for(int i=0; i< phrases.getLength() ; i++ ){
            String triggerPhrase = phrases.item(i).getTextContent();
            if(triggerPhrase!=null){
                triggers.add(triggerPhrase);
                builder.append(triggerPhrase + " ");
            }
        }
        if(triggers.size()==0){throw new GenericException("Error: No triggers found in action specification");}
    }
    public void setSubjects(Element input){
        NodeList subjectsNode = input.getElementsByTagName("entity");
        builder.append("subject: ");
        for(int i=0; i< subjectsNode.getLength() ; i++ ){
            String subjectName = subjectsNode.item(i).getTextContent();
            GameEntity subjectEntity = entitySearch(subjectName);
            if(subjectEntity!=null){
                subjects.add(subjectEntity);
                builder.append(subjectName+" ");
            }
        }
        //if(subjects.size()==0){throw new GenericException("Error: No subjects for action");}  does this hold true?
    }
    public void setConsumed(Element input) throws GenericException {
        builder.append("consumed: ");
        consumed = extractEntity(input);
    }
    public void setProduced(Element input) throws GenericException {
        builder.append("produced: ");
        produced = extractEntity(input);
    }
    private GameEntity extractEntity(Element input) throws GenericException {
        NodeList phrases = input.getElementsByTagName("entity");
        if(phrases.getLength()==0){return null;} //again, can this be 0?
        String entityName = phrases.item(0).getTextContent();
        GameEntity entity = entitySearch(entityName);
        if(entity==null){throw new GenericException("Error: unknown entity in action initiation");}//

        builder.append(entityName+" ");
        return entity;
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
    public String getAsString(){
        return builder.toString();
    }
    public boolean isTrigger(String token){
        for(String string : triggers){
            if(string.equalsIgnoreCase(token)){return true;}
        }
        return false;
    }
    public void checkEntities(ArrayList<GameEntity> entities) throws GenericException {
        if (actionType != ActionType.nonstandard) {
            return;
        }
        for(GameEntity entity : entities){
            boolean hasAppeared=false;
            if(entity.equals(this)){
                hasAppeared = true;
            }
            for(GameEntity subject : subjects){
                if(entity.equals(subject)){
                    hasAppeared =true;
                }
            }
            if(!hasAppeared){
                throw new GenericException("Error: specified entity appears in the scope of an action not currently triggered");
            }
        }
    }
    public void setActionLook(){
        actionType = ActionType.look;
        triggers.add("look");
    }
    public void setActionGet(){
        actionType = ActionType.get;
        triggers.add("get");
    }
    public void setActionDrop(){
        actionType = ActionType.drop;
        triggers.add("drop");
    }
    public void setActionInv(){
        actionType = ActionType.inv;
        triggers.add("inv");
        triggers.add("inventory");
    }
    public void setActionGoTo(){
        actionType = ActionType.goTo;
        triggers.add("goto");
    }
    public void setActionTypeNonstandard(){
        actionType = ActionType.nonstandard;
    }

    public String execute(Player player,ArrayList<Player> players, ArrayList<GameEntity> specifiedEntities) throws GenericException {
        switch(actionType){
            case nonstandard:
                if(consumed!=null) {
                    if (!(player.remove(consumed) || player.getLocation().remove(consumed))) {
                        throw new GenericException("You do not have the items to perform this action.");
                    }
                }
                if(produced!=null) {
                    player.add(produced);
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
                processInv(player);
                break;
            case look:
                processLook(player,players);
                break;
        }
        return narration;
    }
    private void processGoTo(Player player ,ArrayList<GameEntity> specifiedEntities) throws GenericException {
       if(specifiedEntities.size()!=1){throw new GenericException("Error: only one entity can be specified in a goto");}
       for(Location location : player.getLocation().getDestinations()){
           if(location.equals(specifiedEntities.get(0))){
               player.setLocation(location);
               narration = "You are now in " + location.getName();
               return;
           }
       }
       throw new GenericException("Error: you cannot get there from here");
    }
    private void processGet(Player player ,ArrayList<GameEntity> specifiedEntities)throws GenericException{
        if(specifiedEntities.size()!=1){throw new GenericException("Error: only one entity can be specified in a get");}
        player.getItem(specifiedEntities.get(0));
        player.getLocation().remove(specifiedEntities.get(0));
        narration = "You picked up the " + specifiedEntities.get(0).getName();
    }
    private void processDrop(Player player ,ArrayList<GameEntity> specifiedEntities) throws GenericException {
        if(specifiedEntities.size()!=1){throw new GenericException("Error: only one entity can be specified in a drop");}
        player.dropItem(specifiedEntities.get(0));
        narration = "You dropped up the " + specifiedEntities.get(0).getName();
    }
    private void processInv(Player player){
        narration = "The items in your inventory are as follows:\n" + player.getInvAsString();

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
        narration = "You look around "+player.getLocation().getDescription() +" and see:\n"+ player.getLocation().getLocationContentsAsString();
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
    nonstandard
}

