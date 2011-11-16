package tv.notube.commons.storage.kvs.mybatis;

import tv.notube.commons.storage.kvs.mybatis.mappers.KVSMapper;
import org.apache.ibatis.session.SqlSession;
import tv.notube.commons.storage.model.Query;
import tv.notube.commons.storage.model.fields.Bytes;
import tv.notube.commons.storage.model.fields.StringField;

import java.util.List;
import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class KVStoreDao extends ConfigurableDao {

    public KVStoreDao(Properties properties) {
        super(properties);
    }

    public void insertObject(String table, String key, byte[] bytes,
                             StringField[] fields) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        KVSMapper mapper = session.getMapper(KVSMapper.class);
        try {
            mapper.insertObject(table, key, bytes);
            for (StringField field : fields) {
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

    public List<StringField> getFields(String table, String key) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        KVSMapper mapper = session.getMapper(KVSMapper.class);
        List<StringField> fields;
        try {
            fields = mapper.selectFieldsByKey(table, key);
        } finally {
            session.close();
        }
        return fields;
    }

    public List<String> selectByQuery(String table, Query query) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        KVSMapper mapper = session.getMapper(KVSMapper.class);
        List<String> keys;
        try {
            keys = mapper.selectByQuery(table, query.compile());
        } finally {
            session.close();
        }
        return keys;
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