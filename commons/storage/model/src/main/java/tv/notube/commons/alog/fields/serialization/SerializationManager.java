package tv.notube.commons.alog.fields.serialization;

import tv.notube.commons.alog.fields.Bytes;

import java.io.*;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SerializationManager {

    public Bytes serialize(Object serializable)
            throws SerializationManagerException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(baos);
        } catch (IOException e) {
            throw new SerializationManagerException(
                    "Error while serializing object", e
            );
        }
        try {
            objectOutputStream.writeObject(serializable);
        } catch (IOException e) {
            throw new SerializationManagerException(
                    "Error while writing serialized object", e
            );
        }
        Bytes bytes = new Bytes();
        bytes.setBytes(baos.toByteArray());
        try {
            return bytes;
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing stream", e);
            }
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing stream", e);
            }
        }
    }

    public Object deserialize(byte[] bytes)
            throws SerializationManagerException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(bais);
        } catch (IOException e) {
            throw new SerializationManagerException("Error while " +
                    "deserializing the object", e);
        }
        try {
            return objectInputStream.readObject();
        } catch (IOException e) {
            throw new SerializationManagerException(
                    "I/O error while reading bytes",
                    e
            );
        } catch (ClassNotFoundException e) {
            throw new SerializationManagerException(
                    "Class error while reading bytes",
                    e
            );
        } finally {
            try {
                bais.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing stream", e);
            }
            try {
                objectInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing stream", e);
            }
        }
    }
}
