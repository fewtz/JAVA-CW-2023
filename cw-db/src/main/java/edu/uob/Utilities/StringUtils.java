package edu.uob.Utilities;

public abstract class StringUtils {
    public static boolean isStringLiteral(String token,StringBuilder builder){
        if((token.charAt(0)!=39)){return false;}
        for(int i=1;i<token.length()-1;i++) {
            char character = token.charAt(i);
            if(character==34||character==39||character==124){return false;}
            builder.append(character);
        }
        return (token.charAt(token.length()-1)==39);
    }
    public static boolean isBooleanLiteral(String value) {
        if (value.equals("TRUE")){
            return true;
        }
        return value.equals("FALSE");
    }

    public static boolean isFloatLiteral(String value){
        if (notValFirstValue(value)) return false;
        boolean passedPeriod=false;
        for(int i=1;i<value.length();i++){
            char character = value.charAt(i);
            if(!(Character.isDigit(character)||character=='.')){return false;}
            if(character=='.') {if(!passedPeriod) {passedPeriod = true;} else {return false;}
            }
        }

        return true;
    }
    public static boolean isIntegerLiteral(String value){
        if (notValFirstValue(value)) return false;
        for(int i=1;i<value.length();i++){
            char character = value.charAt(i);
            if(!(Character.isDigit(character))){return false;}
        }

        return true;
    }
    private static boolean notValFirstValue(String value) {
        char firstValue =value.charAt(0);
        if(firstValue=='+'){return false;}
        else if (firstValue=='-') {return false;}
        else return !Character.isDigit(firstValue);
    }
    public static valueType checkType(String value){
        if(isIntegerLiteral(value)){
            return valueType.INTEGER;
        }
        if(isFloatLiteral(value)){
            return valueType.FLOAT;
        }
        if(isBooleanLiteral(value)){
            return valueType.BOOLEAN;
        }
        return valueType.STRING;
    }
}
