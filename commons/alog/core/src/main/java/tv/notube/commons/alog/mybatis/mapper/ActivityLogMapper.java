package tv.notube.commons.alog.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;
import tv.notube.commons.alog.Activity;
import tv.notube.commons.alog.Field;

import java.util.Collection;
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

    public List<Field> selectActivityStringFields(
            @Param("id") UUID id
    );

    public List<Field> selectActivityIntegerFields(
            @Param("id") UUID activityId
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

}
