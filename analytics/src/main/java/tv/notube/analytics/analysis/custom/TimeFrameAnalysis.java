package tv.notube.analytics.analysis.custom;

import org.joda.time.DateTime;
import tv.notube.analytics.analysis.*;
import tv.notube.commons.configuration.analytics.AnalysisDescription;
import tv.notube.commons.configuration.analytics.MethodDescription;
import tv.notube.commons.storage.model.Activity;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.ActivityLogException;
import tv.notube.commons.storage.model.fields.DatetimeField;
import tv.notube.commons.storage.model.fields.Field;
import tv.notube.commons.storage.model.fields.StringField;
import tv.notube.commons.storage.model.fields.URLField;

import java.util.*;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TimeFrameAnalysis extends StorageAnalysis {

    public TimeFrameAnalysis(ActivityLog alog, String name, String description) {
        super(alog, name, description);
    }

    public AnalysisResult run(String owner) throws AnalysisException {
        // call for the activities of the last 31 days
        // group them for days
        DateTime from = new DateTime().minusDays(31);
        Activity activities[];
        try {
            activities = alog.filter(from, new DateTime(), owner, query);
        } catch (ActivityLogException e) {
            throw new AnalysisException("Error while filtering from alog", e);
        }
        Map<Integer, List<Activity>> activitiesPerDay =
                new HashMap<Integer, List<Activity>>();
        for (Activity activity : activities) {
            Field fields[];
            try {
                fields = alog.getFields(activity.getId());
            } catch (ActivityLogException e) {
                throw new AnalysisException(
                        "Error while getting fields for '" + activity.getId() + "'",
                        e
                );
            }
            DateTime activityDate = getDate(fields);
            put(activityDate, activity, activitiesPerDay);
        }
        TimeFrameAnalysisResult result = new TimeFrameAnalysisResult(
                new DateTime()
        );
        for (int day : activitiesPerDay.keySet()) {
            List<Activity> dayActivities = activitiesPerDay.get(day);
            ActivityAnalysisResult aar = analizeActivity(dayActivities);
            result.addAnalysis(day, aar);
        }
        return result;
    }

    private ActivityAnalysisResult analizeActivity(List<Activity> dayActivities)
            throws AnalysisException {
        ActivityAnalysisResult result = new ActivityAnalysisResult(
                new DateTime()
        );
        for (Activity activity : dayActivities) {
            Field fields[];
            try {
                fields = alog.getFields(activity.getId());
            } catch (ActivityLogException e) {
                throw new AnalysisException(
                        "Error while getting field for '" + activity.getId() + "'",
                        e
                );
            }
            for (Field field : fields) {
                if (field instanceof StringField) {
                    StringField sf = (StringField) field;
                    if (sf.getName().equals("verb")) {
                        result.add(sf.getValue());
                    }
                }
                if (field instanceof URLField) {
                    URLField sf = (URLField) field;
                    if (sf.getName().equals("service")) {
                        result.add(sf.getValue());
                    }
                }
            }
        }
        return result;
    }

    private void put(
            DateTime activityDate,
            Activity activity,
            Map<Integer, List<Activity>> activitiesPerDay
    ) {
        int day = activityDate.dayOfMonth().get();
        if (activitiesPerDay.containsKey(day)) {
            activitiesPerDay.get(day).add(activity);
        } else {
            List<Activity> activities = new ArrayList<Activity>();
            activities.add(activity);
            activitiesPerDay.put(day, activities);
        }
    }

    private DateTime getDate(Field fields[]) {
        for (Field field : fields) {
            if (field.getName().equals("date")) {
                DatetimeField datetimeField = (DatetimeField) field;
                return datetimeField.getValue();
            }
        }
        return null;
    }

    @Override
    public AnalysisDescription getAnalysisDescription() {
        MethodDescription getStatistics;
        getStatistics = new MethodDescription(
                "getStatistics",
                "this method returns activity statistics day by day of the last month",
                new String[] { "java.lang.Integer" }
        );
        Set<MethodDescription> tfdMds = new HashSet<MethodDescription>();
        tfdMds.add(getStatistics);
        return new AnalysisDescription(
                getName(),
                getDescription(),
                getQuery(),
                TimeFrameAnalysis.class.getCanonicalName(),
                TimeFrameAnalysisResult.class.getCanonicalName(),
                tfdMds
        );
    }

}
