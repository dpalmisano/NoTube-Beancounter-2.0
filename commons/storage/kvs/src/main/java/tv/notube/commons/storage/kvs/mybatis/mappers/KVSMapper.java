package tv.notube.commons.storage.kvs.mybatis.mappers;

import org.apache.ibatis.annotations.Param;
import tv.notube.commons.storage.model.fields.Bytes;
import tv.notube.commons.storage.model.fields.StringField;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface KVSMapper {

    public Bytes selectByKey(
            @Param("kvstable") String table,
            @Param("key") String key);

    public void insertObject(
            @Param("kvstable") String table,
            @Param("key") String key,
            @Param("object") byte[] bytes
    );

    public void insertField(
            @Param("kvstable") String table,
            @Param("key") String key,
            @Param("name") String field,
            @Param("value") String value
    );

    public List<StringField> selectFieldsByKey(
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

    public List<String> selectByQuery(
            @Param("kvstable") String table,
            @Param("query") String query
    );

    public List<String> selectByTable(
            @Param("kvstable") String table
    );

    public List<String> selectByQueryWithLimit(
            @Param("kvstable") String table,
            @Param("query") String query,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
