package tv.notube.analytics.analysis.custom;

import org.joda.time.DateTime;
import tv.notube.analytics.analysis.AnalysisException;
import tv.notube.analytics.analysis.AnalysisResult;
import tv.notube.analytics.analysis.StorageAnalysis;
import tv.notube.commons.storage.model.Activity;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.ActivityLogException;
import tv.notube.commons.storage.model.fields.Field;
import tv.notube.commons.storage.model.fields.StringField;
import tv.notube.commons.storage.model.fields.URLField;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ActivityAnalysis extends StorageAnalysis {

    public ActivityAnalysis(
            ActivityLog alog,
            String name,
            String description
    ) {
        super(alog, name, description);
    }

    public AnalysisResult run(String owner) throws AnalysisException {
        Activity[] activities;
        try {
            activities = alog.filter(new DateTime(), owner, query);
        } catch (ActivityLogException e) {
            throw new AnalysisException("Error while filtering from alog", e);
        }
        ActivityAnalysisResult result = new ActivityAnalysisResult();
        for (Activity activity : activities) {
            Field fields[];
            try {
                fields = alog.getFields(activity.getId());
            } catch (ActivityLogException e) {
                throw new AnalysisException(
                        "Error while getting field for '" + activity.getId() + "'",
                        e
                );
            }
            for(Field field : fields) {
                if(field instanceof StringField) {
                    StringField sf = (StringField) field;
                    if(sf.getName().equals("verb")) {
                        result.add(sf.getValue());
                    }
                }
                if(field instanceof URLField) {
                    URLField sf = (URLField) field;
                    if(sf.getName().equals("service")) {
                        result.add(sf.getValue());
                    }
                }
            }
        }
        return result;
    }
}
