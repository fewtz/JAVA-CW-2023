package edu.uob;

public class DataRow {
    private int size;
    String[] DataPoints;
    private int maxSize;
    private String DataRowAsString;
    DataRow(String Input,int DesiredSize){
        size =0;
        maxSize = 10;
        DataPoints = new String[maxSize];
        DataRowAsString = "";
        MakeRow(Input);
        CheckSize(DesiredSize);
    }
    public void addValue(String datapoint){
        DataPoints[size++] = datapoint;
    }
    public void removeValue(int i){
        while(i<size){
            DataPoints[i++] = DataPoints[i+1];
        }
        size--;
    }

    private void MakeRow(String Input){
        String datapoint = "";
        char currentChar;
        for(int i=0;i<Input.length();i++) {
            currentChar = Input.charAt(i);
            if (currentChar != '\t') {
                datapoint += currentChar;
            } else {
                //INSERT DATAPOINT
                DataPoints[size++] = datapoint;
                DataRowAsString += datapoint +" ";
                datapoint = "";
                if(size>=maxSize){
                    makeArrayBigger();
                }
            }
        }
        DataRowAsString += "\n";
    }
    private void makeArrayBigger(){
        String[] newList = new String[maxSize*2];
        System.arraycopy(DataPoints, 0, newList, 0, DataPoints.length);
        DataPoints = newList;
    }
    private void CheckSize(int DesiredSize){
        if(DesiredSize!=size){
            throw new RuntimeException("ERROR: inconsistent table width during creation");
        }
    }

    public String getDataRowAsString(){
        return DataRowAsString;
    }
}
