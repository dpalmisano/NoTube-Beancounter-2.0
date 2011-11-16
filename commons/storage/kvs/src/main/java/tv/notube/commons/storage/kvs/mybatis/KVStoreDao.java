package tv.notube.commons.storage.kvs.mybatis;

import tv.notube.commons.storage.kvs.Field;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.mybatis.mappers.KVSMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class KVStoreDao extends ConfigurableDao {

    public KVStoreDao(Properties properties) {
        super(properties);
    }

    public void insertObject(String table, String key, byte[] bytes, Field[] fields) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        KVSMapper mapper = session.getMapper(KVSMapper.class);
        try {
            mapper.insertObject(table, key, bytes);
            for (Field field : fields) {
                mapper.insertField(table, key, field.getName(), field.getValue());
            }
            session.commit();
        } finally {
            session.close();
        }
    }

    public byte[] getObject(String table, String key) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        KVSMapper mapper = session.getMapper(KVSMapper.class);
        Bytes bytes;
        try {
            bytes = mapper.selectByKey(table, key);
        } finally {
            session.close();
        }
        return bytes != null ? bytes.getBytes() : null;
    }

    public List<Field> getFields(String table, String key) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        KVSMapper mapper = session.getMapper(KVSMapper.class);
        List<Field> fields;
        try {
            fields = mapper.selectFieldsByKey(table, key);
        } finally {
            session.close();
        }
        return fields;
    }

    public List<String> selectByFields(String table, Field[] fields) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        KVSMapper mapper = session.getMapper(KVSMapper.class);
        List<String> keys = new ArrayList<String>();
        try {
            for (Field field : fields) {
                keys.addAll(mapper.selectByField(table, field.getName(), field.getValue()));
            }
        } finally {
            session.close();
        }
        return keys;
    }

    public List<String> selectByFieldRange(String table, Field field, KVStore.Math op) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        KVSMapper mapper = session.getMapper(KVSMapper.class);
        try {
            if (op.equals(KVStore.Math.LESS)) {
                return mapper.selectByFieldRangeLess(table, field.getName(), Long.parseLong(field.getValue()));
            } else {
                return mapper.selectByFieldRangeGreat(table, field.getName(), Long.parseLong(field.getValue()));
            }
        } finally {
            session.close();
        }
    }

    public void deleteByKey(String table, String key) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        KVSMapper mapper = session.getMapper(KVSMapper.class);
        try {
            mapper.deleteObject(table, key);
            mapper.deleteFields(table, key);
        } finally {
            session.commit();
            session.close();
        }
    }

}