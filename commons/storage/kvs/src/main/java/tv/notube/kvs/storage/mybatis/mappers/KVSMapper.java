package tv.notube.kvs.storage.mybatis.mappers;

import tv.notube.kvs.storage.Field;
import tv.notube.kvs.storage.mybatis.Bytes;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface KVSMapper {

    public Bytes selectByKey(
            @Param("kvstable") String table,
            @Param("key") String key);

    public List<String> selectByField(
            @Param("kvstable") String table,
            @Param("field") String field,
            @Param("value") String value
    );

    public void insertObject(
            @Param("kvstable") String table,
            @Param("key") String key,
            @Param("object") byte[] bytes
    );

    public void insertField(
            @Param("kvstable") String table,
            @Param("key") String key,
            @Param("field") String field,
            @Param("value") String value
    );

    public List<Field> selectFieldsByKey(
            @Param("kvstable") String table,
            @Param("key") String key
    );

    public void deleteObject(
            @Param("kvstable") String table,
            @Param("key") String key
    );

    public void deleteFields(
            @Param("kvstable") String table,
            @Param("key") String key
    );

    public List<String> selectByFieldRangeGreat(
            @Param("kvstable") String table,
            @Param("field") String name,
            @Param("value") long value
    );

    public List<String> selectByFieldRangeLess(
            @Param("kvstable") String table,
            @Param("field") String name,
            @Param("value") long value
    );

}
