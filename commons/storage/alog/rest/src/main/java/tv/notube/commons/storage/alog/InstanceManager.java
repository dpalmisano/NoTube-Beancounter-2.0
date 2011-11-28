package tv.notube.commons.storage.alog;


import tv.notube.commons.storage.model.ActivityLog;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InstanceManager {

    private ActivityLog activityLog;


    public InstanceManager() {
        activityLog = LoaderInstanceManager.getInstance().getActivityLog();
    }

    ActivityLog getActivityLog() {
        return activityLog;
    }

}