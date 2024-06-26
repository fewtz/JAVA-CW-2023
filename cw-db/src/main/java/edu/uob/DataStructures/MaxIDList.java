package edu.uob.DataStructures;
import edu.uob.Utilities.GenericException;
import java.io.*;
import java.util.ArrayList;

public class MaxIDList implements java.io.Serializable{
    @Serial private static final long serialVersionUID = 6;
    private File MaxIdFile;
    private ArrayList<String> Tables;
    private ArrayList<Integer> MaxIDs;
    public MaxIDList(File inputFile) throws GenericException, IOException {
        MaxIdFile = inputFile;
        Tables = new ArrayList<>();
        MaxIDs = new ArrayList<>();
        initArrays();
    }
    private void initArrays() throws GenericException, IOException {
        BufferedReader buffReader;
        try {
            FileReader reader = new FileReader(MaxIdFile);
            buffReader = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            throw new GenericException("[ERROR] : File not found");
        }
        String nextLine = buffReader.readLine();
        while(nextLine!=null){
            addEntry(nextLine);
            nextLine = buffReader.readLine();
        }
        buffReader.close();
    }
    private void addEntry(String line){
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<line.length();i++){
            char character = line.charAt(i);
            if(character == ' '){
                Tables.add(builder.toString());
                builder = new StringBuilder();
            } else{
                builder.append(character);
            }
        }
        MaxIDs.add(Integer.valueOf(builder.toString()));
    }
    public void setMaxID(String name, int MaxID) throws GenericException {
        int i=-1;
        for(String title : Tables){
            if(name.equals(title)){
                i = Tables.indexOf(title);
            }
        }
        if(i<0){throw new GenericException("[ERROR] : No max ID record found");}
        MaxIDs.set(i,MaxID);
        writeToDisk();
    }
    public int getMaxID(String name) throws GenericException {
        int i=-1;
        for(String title : Tables){
            if(name.equals(title)){
                i = Tables.indexOf(title);
            }
        }
        if(i<0){throw new GenericException("[ERROR] : No max ID record found");}
        return MaxIDs.get(i);
    }
    public void createNewTableEntry(String name) throws GenericException {
        Tables.add(name);
        MaxIDs.add(0);
        writeToDisk();
    }
    public void writeToDisk() throws GenericException {
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<Tables.size();i++){
            builder.append(Tables.get(i) + " "  +MaxIDs.get(i).toString()  + "\n");
        }
        try {
            FileWriter fileWriter = new FileWriter(MaxIdFile,false);
            fileWriter.write(builder.toString());
            fileWriter.close();
        }catch(Exception IOException){
            throw new GenericException("[ERROR] : Unable to write to disk");
        }
    }
}
