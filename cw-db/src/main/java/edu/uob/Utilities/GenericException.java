package edu.uob;

public class GenericException extends Exception{
    String message;
    GenericException(String errorMessage){
        message = errorMessage;
    }
    @Override
    public String toString(){
        return message;
    }
}
