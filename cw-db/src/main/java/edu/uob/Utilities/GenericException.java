package edu.uob.Utilities;

import java.io.Serial;
public class GenericException extends Exception {
    @Serial private static final long serialVersionUID = 1;
    String message;
    public GenericException(String errorMessage){
        message = errorMessage;
    }
    @Override
    public String toString(){
        return message;
    }
}
