package tv.notube.commons.alog.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.joda.time.DateTime;
import tv.notube.commons.alog.Activity;
import tv.notube.commons.alog.Field;
import tv.notube.commons.alog.IntegerField;
import tv.notube.commons.alog.StringField;
import tv.notube.commons.alog.mybatis.mapper.ActivityLogMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ActivityLogDao extends ConfigurableDao {

    public ActivityLogDao(Properties properties) {
        super(properties);
    }

    public void insertActivity(Activity activity, Field[] fields) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ActivityLogMapper mapper = session.getMapper(ActivityLogMapper.class);
        try {
            mapper.insertActivity(activity);
            for (Field field : fields) {
                if (field instanceof StringField) {
                    mapper.insertStringField(activity.getId(), field);
                    continue;
                } else if (field instanceof IntegerField) {
                    mapper.insertIntegerField(activity.getId(), field);
                    continue;
                }
                throw new IllegalArgumentException("Field with type: '" +
                        field.getClass() +
                        "' is not supported");
            }
            session.commit();
        } finally {
            session.close();
        }
    }

    public Field[] selectActivityFields(UUID activityId) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ActivityLogMapper mapper = session.getMapper(ActivityLogMapper.class);
        List<Field> fields = new ArrayList<Field>();
        try {
            fields.addAll(mapper.selectActivityStringFields(activityId));
            fields.addAll(mapper.selectActivityIntegerFields(activityId));
        } finally {
            session.close();
        }
        return fields.toArray(new Field[fields.size()]);
    }

    public Activity[] selectActivityByDateRange(DateTime from, DateTime to) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ActivityLogMapper mapper = session.getMapper(ActivityLogMapper.class);
        List<Activity> activities;
        try {
            activities = mapper.selectActivityByDateRange(from, to);
        } finally {
            session.close();
        }
        return activities.toArray(new Activity[activities.size()]);
    }

    public Activity[] selectActivityByDateRangeAndOwner(DateTime from, DateTime to, String owner) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ActivityLogMapper mapper = session.getMapper(ActivityLogMapper.class);
        List<Activity> activities;
        try {
            activities = mapper.selectActivityByDateRangeAndOwner(from, to, owner);
        } finally {
            session.close();
        }
        return activities.toArray(new Activity[activities.size()]);
    }

    public Activity[] selectActivityByOwner(String owner) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ActivityLogMapper mapper = session.getMapper(ActivityLogMapper.class);
        List<Activity> activities;
        try {
            activities = mapper.selectActivityByOwner(owner);
        } finally {
            session.close();
        }
        return activities.toArray(new Activity[activities.size()]);
    }

    public void deleteActivitiesByDateRange(DateTime from, DateTime to) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ActivityLogMapper mapper = session.getMapper(ActivityLogMapper.class);
        List<Activity> activities = mapper.selectActivityByDateRange(from, to);
        try {
            for(Activity activity : activities) {
                mapper.deleteActivityStringFields(activity.getId());
                mapper.deleteActivityIntegerFields(activity.getId());
            }
            mapper.deleteActivitiesByDateRange(from, to);
        } finally {
            session.commit();
            session.close();
        }
    }

    public void deleteActivitiesByOwner(String owner) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ActivityLogMapper mapper = session.getMapper(ActivityLogMapper.class);
        List<Activity> activities = mapper.selectActivityByOwner(owner);
        try {
            for(Activity activity : activities) {
                mapper.deleteActivityStringFields(activity.getId());
                mapper.deleteActivityIntegerFields(activity.getId());
            }
            mapper.deleteActivitiesByOwner(owner);
        } finally {
            session.commit();
            session.close();
        }
    }

    public void deleteActivitiesByDateRangeAndOwner(DateTime from, DateTime to, String owner) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        ActivityLogMapper mapper = session.getMapper(ActivityLogMapper.class);
        List<Activity> activities = mapper.selectActivityByDateRangeAndOwner(
                from,
                to,
                owner
        );
        try {
            for(Activity activity : activities) {
                mapper.deleteActivityStringFields(activity.getId());
                mapper.deleteActivityIntegerFields(activity.getId());
            }
            mapper.deleteActivitiesByDateRangeAndOwner(from, to, owner);
        } finally {
            session.commit();
            session.close();
        }
    }

}
