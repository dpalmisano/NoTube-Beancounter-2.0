package tv.notube.analytics.analysis;

import tv.notube.commons.storage.model.ActivityLog;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class StorageAnalysis extends Analysis {

    protected ActivityLog alog;

    public StorageAnalysis(ActivityLog alog, String name, String description) {
        super(name, description);
        this.alog = alog;
    }

}
