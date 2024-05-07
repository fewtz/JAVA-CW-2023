package edu.uob.engine.parsers;
import edu.uob.engine.GameAction;

import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import edu.uob.engine.entities.GameEntity;
import edu.uob.engine.entities.Location;
import edu.uob.utilities.GenericException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ActionParser extends ParserFramework {
    ArrayList<GameEntity> allEntities;
    Location storeRoom;
    ArrayList<Location> locations;
    public ActionParser(Location storeRoomInput,ArrayList<Location> locationsInput){
        storeRoom = storeRoomInput;
        locations = locationsInput;
    }
    public ArrayList<GameAction> parse(File actionFile, ArrayList<GameEntity> entities) throws GenericException {
        allEntities = entities;

        NodeList actions = extractActions(actionFile);

        ArrayList<GameAction> actionList = new ArrayList<>();
        for(int i=0; i<actions.getLength();i++){
            if(i%2!=0){
                Element action = (Element) actions.item(i);
                actionList.add(extractAction(action));
            }
        }
        return actionList;
    }
    private NodeList extractActions(File actionFile) throws GenericException {
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(actionFile);
        } catch (ParserConfigurationException e) {
            throw new GenericException("ERROR: Action parser configuration error");
        } catch (SAXException e) {
            throw new GenericException("ERROR: Action parser failure");
        } catch (IOException e) {
            throw new GenericException("ERROR: IOException when parsing action file");
        }
        Element root = document.getDocumentElement();
        return root.getChildNodes();
    }

    private GameAction extractAction(Element action) throws GenericException {
        GameAction newAction = new GameAction(allEntities,storeRoom,locations);
        newAction.setTriggers((Element)action.getElementsByTagName("triggers").item(0));
        newAction.setSubjects((Element)action.getElementsByTagName("subjects").item(0));
        newAction.setConsumed((Element)action.getElementsByTagName("consumed").item(0));
        newAction.setProduced((Element)action.getElementsByTagName("produced").item(0));
        newAction.setNarration((Element)action.getElementsByTagName("narration").item(0));
        newAction.setActionTypeNonstandard();
        return newAction;
    }
}
