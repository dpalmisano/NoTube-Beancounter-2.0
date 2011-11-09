package tv.notube.kvs.storage;

import java.util.List;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface KVStore {

    public enum Boolean {
        AND,
        OR
    }

    public enum Math {
        GREAT,
        LESS
    }

    public List<String> search(String table, Boolean op, Field... field)
        throws KVStoreException;

    public List<String> search(String table, Math op, Field field)
        throws KVStoreException;

    public Object getValue(String table, String key)
            throws KVStoreException;

    public Field[] getFields(String table, String key)
        throws KVStoreException;

    public void setValue(String table, String key, Object object, Field... fields)
            throws KVStoreException;

    void deleteValue(String table, String key) throws KVStoreException;

}
