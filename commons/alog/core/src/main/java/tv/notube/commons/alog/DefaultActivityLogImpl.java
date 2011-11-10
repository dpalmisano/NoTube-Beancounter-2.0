package tv.notube.commons.alog;

import org.joda.time.DateTime;
import tv.notube.commons.alog.mybatis.ActivityLogDao;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultActivityLogImpl implements ActivityLog {

    private ActivityLogDao dao;

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

    public Activity[] filter(DateTime from, DateTime to, String owner)
            throws ActivityLogException {
        return dao.selectActivityByDateRangeAndOwner(from, to, owner);
    }

    public Activity[] filter(DateTime from, DateTime to, String owner, Query query)
            throws ActivityLogException {
        throw new UnsupportedOperationException("NIY");
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
}
