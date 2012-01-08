package tv.notube.analytics.analysis.custom;

import org.joda.time.DateTime;
import tv.notube.analytics.analysis.*;
import tv.notube.commons.storage.model.Activity;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.ActivityLogException;
import tv.notube.commons.storage.model.fields.DatetimeField;
import tv.notube.commons.storage.model.fields.Field;

import java.util.HashSet;
import java.util.Set;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class WhenYouWatchOrListenAnalysis extends StorageAnalysis {

    public WhenYouWatchOrListenAnalysis(ActivityLog alog, String name, String description) {
        super(alog, name, description);
    }

    public AnalysisResult run(String owner) throws AnalysisException {
        // call for the activities WATCHED or LISTENED
        // group them for time slice
        Activity activities[];
        try {
            activities = alog.filter(new DateTime(), owner, query);
        } catch (ActivityLogException e) {
            throw new AnalysisException("Error while filtering from alog", e);
        }
        int[] slice = {0, 0, 0, 0};
        // 0 - 6
        // 6 - 12
        // 12 - 18
        // 18 - 24
        for(Activity activity : activities) {
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
            filter(activityDate, slice);
        }
        WhenYouWatchOrListenAnalysisResult result =
                new WhenYouWatchOrListenAnalysisResult(new DateTime(), slice);
        return result;
    }

    private void filter(DateTime activityDate, int[] slice) {
        if(activityDate == null) {
            return;
        }
        int h = activityDate.getHourOfDay();
        if(h >= 0 && h < 6) {
            slice[0]++;
        } else if(h >= 6 && h < 12) {
            slice[1]++;
        } else if(h >= 12 && h < 18) {
            slice[2]++;
        } else {
            slice[3]++;
        }
        //To change body of created methods use File | Settings | File Templates.
    }

    public AnalysisDescription getAnalysisDescription() {
        MethodDescription getPercentage;
        getPercentage = new MethodDescription(
                "getPercentages",
                "returns watch or listen percentages per each time slice 0 to 3",
                new String[] { "java.lang.Integer" }
        );
        MethodDescription getNumbers;
        getNumbers = new MethodDescription(
                "getNumbers",
                "returns watch or listen absolute numbers per each time slice 0 to 3",
                new String[] { "java.lang.Integer" }
        );
        Set<MethodDescription> mds = new HashSet<MethodDescription>();
        mds.add(getPercentage);
        mds.add(getNumbers);
        return new AnalysisDescription(
                getName(),
                getDescription(),
                getQuery(),
                WhenYouWatchOrListenAnalysis.class.getCanonicalName(),
                WhenYouWatchOrListenAnalysisResult.class.getCanonicalName(),
                mds
        );
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

}
