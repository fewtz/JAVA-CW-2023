package edu.uob.engine.parsers;

import edu.uob.utilities.GenericException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

abstract class ParserFramework {
    BufferedReader createReader(File entitiesFile) throws GenericException {
        BufferedReader bufferedReader;
        try {
            FileReader reader = new FileReader(entitiesFile);
            bufferedReader = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            throw new GenericException("[ERROR] : File not found");
        }
        return bufferedReader;
    }
}
