package edu.uob.Utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

//TODO Check if this is allowed lmao

//THIS IS NOT CODE THAT I DESIGNED, IT IS A PRE-MADE DEEP COPY PACKAGE FROM THE INTERNET!!!
public class DeepObjectCopier {
    public static Object copy(Object orig) throws GenericException {
        Object obj = null;
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        }
        catch(IOException | ClassNotFoundException e) {
            throw new GenericException("[ERROR] : Unable to copy table in join table creation");
        }
        return obj;
    }
}
