package tv.notube.kvs.storage.serialization;

import java.io.*;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SerializationManager {

    private ObjectOutputStream objectOutputStream;

    private ObjectInputStream objectInputStream;

    public void serialize(Object serializable, OutputStream outputStream)
            throws SerializationManagerException {
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            throw new SerializationManagerException(
                    "Error while instantiating the ObjectOutputStream",
                    e
            );
        }
        try {
            objectOutputStream.writeObject(serializable);
        } catch (IOException e) {
            throw new SerializationManagerException(
                    "Error while writing the object to be serialized",
                    e
            );
        } finally {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                throw new SerializationManagerException(
                        "Error while closing the ObjectOutputStream",
                        e
                );
            }
        }

    }

    public Serializable deserialize(InputStream inputStream)
            throws SerializationManagerException {
        try {
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            throw new SerializationManagerException(
                    "Error while instantiating the ObjectInputStream",
                    e
            );
        }
        try {
            return (Serializable) objectInputStream.readObject();
        } catch (IOException e) {
            throw new SerializationManagerException(
                    "Error while reading the object to be deserialized",
                    e
            );
        } catch (ClassNotFoundException e) {
            throw new SerializationManagerException(
                    "Error while deserializing: cannot find target class",
                    e
            );
        } finally {
            try {
                objectInputStream.close();
            } catch (IOException e) {
                throw new SerializationManagerException(
                        "Error while closing the ObjectInputStream",
                        e
                );
            }
        }
    }

}
