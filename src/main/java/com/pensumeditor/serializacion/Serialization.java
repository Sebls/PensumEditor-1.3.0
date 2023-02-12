package com.pensumeditor.serializacion;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serialization {
    public static boolean ObjectSerialization(String filePath, Serializable object) {
        boolean sw = false;
        try (FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream output = new ObjectOutputStream(fos);) {
            output.writeObject(object);
            sw = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sw;
    }
    public static <E> E ObjectDeserialization(String filePath, Class<E> objectClass) {
        E object = null;
        try (FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream input = new ObjectInputStream(fis);) {
            object = (E) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
