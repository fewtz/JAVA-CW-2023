package edu.uob.Utilities;

public class GenericException extends Exception{
    String message;
    public GenericException(String errorMessage){
        message = errorMessage;
    }
    @Override
    public String toString(){
        return message;
    }
}
