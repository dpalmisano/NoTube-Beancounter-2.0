package tv.notube.commons.alog.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;
import tv.notube.commons.alog.Activity;
import tv.notube.commons.alog.fields.Bytes;
import tv.notube.commons.alog.fields.Field;

import java.util.List;
import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface ActivityLogMapper {

    public void insertActivity(Activity activity);

    public void insertStringField(
            @Param("id") UUID id,
            @Param("field") Field field
    );

    public void insertIntegerField(
            @Param("id") UUID id,
            @Param("field") Field field
    );

    public void insertDatetimeField(
            @Param("id") UUID id,
            @Param("field") Field field
    );

    public void insertURLField(
            @Param("id") UUID id,
            @Param("field") Field field
    );

    public void insertBytesField(
            @Param("id") UUID id,
            @Param("name") String name,
            @Param("value") byte[] field
    );

    public List<Activity> selectActivityByOwner(
            @Param("owner") String owner
    );

    public List<Activity> selectActivityByDateRange(
            @Param("from") DateTime from,
            @Param("to") DateTime to
    );

    public List<Activity> selectActivityByDateRangeAndOwner(
            @Param("from") DateTime from,
            @Param("to") DateTime to,
            @Param("owner") String owner
    );

    public List<Activity> selectActivityByQuery(
            @Param("from") DateTime from,
            @Param("to") DateTime to,
            @Param("owner") String owner,
            @Param("query") String query
    );

    public List<Activity> selectActivityByQueryWithDate(
            @Param("to") DateTime to,
            @Param("owner") String owner,
            @Param("query") String query
    );

    public List<Field> selectActivityStringFields(
            @Param("id") UUID id
    );

    public List<Field> selectActivityIntegerFields(
            @Param("id") UUID id
    );

    public List<Field> selectActivityDatetimeFields(
            @Param("id") UUID id
    );

    public List<Field> selectActivityURLFields(
            @Param("id") UUID id
    );

    public void deleteActivitiesByDateRange(
            @Param("from") DateTime from,
            @Param("to") DateTime to
    );

    public void deleteActivitiesByDateRangeAndOwner(
            @Param("from") DateTime from,
            @Param("to") DateTime to,
            @Param("owner") String owner
    );

    public void deleteActivitiesByOwner(
            @Param("owner") String owner
    );

    public void deleteActivityStringFields(
            @Param("id") UUID activityId
    );

    public void deleteActivityIntegerFields(
            @Param("id") UUID activityId
    );

    public void deleteActivityURLFields(
            @Param("id") UUID id
    );

    public void deleteActivityDatetimeFields(
            @Param("id") UUID id
    );

}
