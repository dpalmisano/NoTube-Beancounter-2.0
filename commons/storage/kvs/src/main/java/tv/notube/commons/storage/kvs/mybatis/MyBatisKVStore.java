package tv.notube.commons.storage.kvs.mybatis;

import org.apache.log4j.Logger;
import tv.notube.commons.storage.kvs.AbstractKVStore;
import tv.notube.commons.storage.kvs.KVStoreException;
import tv.notube.commons.storage.model.Query;
import tv.notube.commons.storage.model.fields.Bytes;
import tv.notube.commons.storage.model.fields.Field;
import tv.notube.commons.storage.model.fields.StringField;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;
import tv.notube.commons.storage.model.fields.serialization.SerializationManagerException;

import java.util.List;
import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class MyBatisKVStore extends AbstractKVStore {

    private static Logger logger = Logger.getLogger(MyBatisKVStore.class);

    private KVStoreDao dao;

    public MyBatisKVStore(
            Properties properties,
            SerializationManager serializationManager
    ) {
        super(serializationManager);
        dao = new KVStoreDao(properties);
    }

    public List<String> search(String table, Query query) throws KVStoreException {
        return dao.selectByQuery(table, query);
    }

    public List<String> search(String table) throws KVStoreException {
        return dao.selectByTable(table);
    }

    public Object getValue(String table, String key) throws KVStoreException {
        byte[] bytes = dao.getObject(table, key);
        if(bytes == null) {
            return null;
        }
        try {
            return serializationManager.deserialize(bytes);
        } catch (SerializationManagerException e) {
            final String errMsg = "Error while deserializing object";
            logger.error(errMsg, e);
            throw new KVStoreException(errMsg, e);
        }
    }

    public StringField[] getFields(String table, String key) throws
            KVStoreException {
        List<StringField> fields = dao.getFields(table, key);
        return fields.toArray(new StringField[fields.size()]);
    }

    public synchronized void setValue(
            String table,
            String key,
            Object object,
            StringField... fields
    ) throws KVStoreException {
        Bytes bytes;
        try {
            bytes = serializationManager.serialize(object);
        } catch (SerializationManagerException e) {
            final String errMsg = "Error while serializing object with (table, key) '(" + table + "," + key + ")'";
            logger.error(errMsg, e);
            throw new KVStoreException(errMsg, e);
        }
        final byte[] serialization = bytes.getBytes();
        dao.insertObject(table, key, serialization, fields);
    }

    public synchronized void deleteValue(String table, String key) throws KVStoreException {
        dao.deleteByKey(table, key);
    }

}
