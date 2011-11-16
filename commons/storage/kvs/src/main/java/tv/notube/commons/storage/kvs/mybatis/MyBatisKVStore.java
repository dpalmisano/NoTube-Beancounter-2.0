package tv.notube.commons.storage.kvs.mybatis;

import org.apache.log4j.Logger;
import tv.notube.commons.storage.kvs.AbstractKVStore;
import tv.notube.commons.storage.kvs.Field;
import tv.notube.commons.storage.kvs.KVStoreException;
import tv.notube.commons.storage.kvs.serialization.SerializationManager;
import tv.notube.commons.storage.kvs.serialization.SerializationManagerException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public List<String> search(String table, Boolean op, Field... fields)
            throws KVStoreException {
        return dao.selectByFields(table, fields);
    }

    public List<String> search(String table, Math op, Field field)
            throws KVStoreException {
        return dao.selectByFieldRange(table, field, op);
    }

    public Object getValue(String table, String key) throws KVStoreException {
        byte[] bytes = dao.getObject(table, key);
        if(bytes == null) {
            return null;
        }
        InputStream is = new ByteArrayInputStream(bytes);
        try {
            return serializationManager.deserialize(is);
        } catch (SerializationManagerException e) {
            throw new KVStoreException("", e);
        }
    }

    public Field[] getFields(String table, String key) throws KVStoreException {
        List<Field> fields = dao.getFields(table, key);
        return fields.toArray(new Field[fields.size()]);
    }

    public synchronized void setValue(String table, String key, Object object, Field... fields)
            throws KVStoreException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            serializationManager.serialize(object, baos);
        } catch (SerializationManagerException e) {
            final String errMsg = "Error while serializing object with (table, key) '(" + table + "," + key + ")'";
            logger.error(errMsg, e);
            throw new KVStoreException(errMsg, e);
        }
        final byte[] serialization = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            throw new KVStoreException("", e);
        }
        dao.insertObject(table, key, serialization, fields);
    }

    public synchronized void deleteValue(String table, String key) throws KVStoreException {
        dao.deleteByKey(table, key);
    }

}
