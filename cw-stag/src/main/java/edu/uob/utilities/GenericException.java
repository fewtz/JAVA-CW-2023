package edu.uob.utilities;
public class GenericException extends Exception {
    private static final long serialVersionUID = 1000000;
    String message;
    public GenericException(String errorMessage){
        message = errorMessage;
    }
    @Override
    public String toString(){
        return "";
    }
}
