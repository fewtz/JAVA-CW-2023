package edu.uob.engine;
import edu.uob.engine.entities.Artefact;
import edu.uob.engine.entities.GameEntity;
import edu.uob.engine.entities.Player;
import edu.uob.utilities.GenericException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
public class GameAction{
    private ArrayList<GameEntity> allEntities;
    private ArrayList<String> triggers = new ArrayList<>();
    private ArrayList<GameEntity> subjects = new ArrayList<>();
    private GameEntity consumed;
    private GameEntity produced;
    private String narration;

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
       // if(entity==null){throw new GenericException("Error: unknown entity in action initiation");}
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
    public void checkEntities(ArrayList<GameEntity> entities){

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
                throw new RuntimeException("Error: specified entity appears in the scope of an action not currently triggered");
            }
        }
    }

    public String execute(Player player) throws GenericException {
        player.remove(consumed);
        player.add(produced);
        return narration;
    }
}
