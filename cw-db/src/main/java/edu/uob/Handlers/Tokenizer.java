package edu.uob.Handlers;

import edu.uob.Utilities.GenericException;

import java.util.ArrayList;

public class Tokenizer {
    ArrayList<String> tokens;
    String command;
    public Tokenizer(String Input) throws GenericException {
        command = Input;
        if(command.isEmpty()){throw new GenericException("[ERROR] : Empty command supplied");}
    }
    public ArrayList<String> tokenize() throws IndexOutOfBoundsException, GenericException {
        tokens = new ArrayList<String>();
        String tempToken="";
        char currentChar;
        int currentCharPlace=0;
        int currentState=0;
        int previousState=0;
        int commandLength = command.length();
        //  YOU HAVE TO FIX THIS LATER THIS IS DISGUSTING
        while(true){
            currentChar=command.charAt(currentCharPlace);
            if(currentState!=3){currentState= isFlushToken(currentChar);}
            if(currentState>0||previousState==2){
                flush(tempToken);
                tempToken="";
            }
            if(currentChar!=' '){tempToken+=currentChar;}
            if(currentCharPlace++>=commandLength-1){
                flush(tempToken);
                if((currentState==3&&tempToken.isEmpty())||tempToken.equals(";")){break;}
                System.out.println("Bad Command, please try again");
                break;
            }
            previousState = currentState;
        }
        combineOperators();
        if(!tokens.get(tokens.size()-1).equals(";")){throw new GenericException("[ERROR] : Missing closing ';'");}
        return tokens;
    }
    private void flush(String tempToken){
        if(!tempToken.isEmpty()){
            tokens.add(tempToken);
        }
    }
    private int isFlushToken(char current){
        if(current == ' ' || current == ')' ) {
            return 1;
        } else if ( current == '=' || current == '>' || current == '<' || current == '(' || current == ','|| current == '!'){
            return 2;
        }else if( current == ';'){
            return 3;
        }
        return 0;
    }
    private void combineOperators(){
        boolean isPrevOp=false;
        boolean isCurrOp;
        String prevToken="";
        String currToken;
        for(int i=tokens.size()-1;i>0;i--){
            currToken = tokens.get(i);
            isCurrOp=isOperator(currToken);
            if(isCurrOp&&isPrevOp){
                tokens.remove(i+1);
                tokens.set(i,currToken+prevToken);
            }
            prevToken = currToken;
            isPrevOp = isCurrOp;
        }
    }

    private boolean isOperator(String token){
        return token.equals("<")||token.equals(">")||token.equals("=")||token.equals("!");
    }
    public void printTokens(){
        for(String token : tokens){
            System.out.println(token);
        }
    }
}
