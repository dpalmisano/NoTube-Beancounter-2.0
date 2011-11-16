package tv.notube.commons.alog;

import org.joda.time.DateTime;
import tv.notube.commons.alog.fields.Field;
import tv.notube.commons.alog.mybatis.ActivityLogDao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultActivityLogImpl implements ActivityLog {

    private ActivityLogDao dao;

    public DefaultActivityLogImpl(Properties properties) {
        dao = new ActivityLogDao(properties);
    }

    public void log(String owner, String description, Field... fields)
            throws ActivityLogException {
        Activity activity = new Activity(owner, description);
        dao.insertActivity(activity, fields);
    }

    public Field[] getFields(UUID activityId) throws ActivityLogException {
        return dao.selectActivityFields(activityId);
    }

    public Activity[] filter(DateTime from, DateTime to) throws ActivityLogException {
        return dao.selectActivityByDateRange(from, to);
    }

    public Activity[] filter(DateTime to, String owner, Query query)
            throws ActivityLogException {
        return dao.selectActivityByDateOwnerAndQuery(to, owner, query);
    }

    public Activity[] filter(DateTime to, String owner) throws ActivityLogException {
        return dao.selectActivityByDateOwner(to, owner);
    }

    public Activity[] filter(DateTime from, DateTime to, String owner)
            throws ActivityLogException {
        return dao.selectActivityByDateRangeAndOwner(from, to, owner);
    }

    public Activity[] filter(DateTime from, DateTime to, String owner, Query query)
            throws ActivityLogException {
        return dao.selectActivityByQuery(from, to, owner, query);
    }

    public void delete(DateTime from, DateTime to) throws ActivityLogException {
        dao.deleteActivitiesByDateRange(from, to);
    }

    public void delete(DateTime from, DateTime to, String owner)
            throws ActivityLogException {
        dao.deleteActivitiesByDateRangeAndOwner(from, to, owner);
    }

    public void delete(String owner) throws ActivityLogException {
        dao.deleteActivitiesByOwner(owner);
    }

    public void export(OutputStream outputStream, DateTime from,
                       DateTime to) throws ActivityLogException {
        throw new UnsupportedOperationException("NIY");
    }

    public void backup(InputStream inputStream) throws ActivityLogException {
        throw new UnsupportedOperationException("NIY");
    }

}
