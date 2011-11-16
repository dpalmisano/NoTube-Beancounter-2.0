package tv.notube.commons.storage.kvs;

import tv.notube.commons.storage.kvs.serialization.SerializationManager;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class AbstractKVStore implements KVStore {

    protected SerializationManager serializationManager;

    public AbstractKVStore(SerializationManager serializationManager) {
        this.serializationManager = serializationManager;
    }

    public SerializationManager getSerializationManager() {
        return serializationManager;
    }
}
