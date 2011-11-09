package tv.notube.extension.profilingline;

import tv.notube.commons.model.activity.Activity;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RawData {

    private String username;

    private Map<Activity, List<URI>> linkedActivities
            = new HashMap<Activity, List<URI>>();

    private List<Activity> activities = new ArrayList<Activity>();

    public RawData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Map<Activity, List<URI>> getLinkedActivities() {
        return linkedActivities;
    }

    public void setLinkedActivities(Map<Activity, List<URI>> linkedActivities) {
        this.linkedActivities = linkedActivities;
    }

    public void addLinkedActivity(Activity activity, List<URI> resources) {
        if(linkedActivities.containsKey(activity)) {
            linkedActivities.get(activity).addAll(resources);
        } else {
            linkedActivities.put(activity, resources);
        }
    }

    public void addLinkedActivity(Activity activity, URI resource) {
        if(linkedActivities.containsKey(activity)) {
            linkedActivities.get(activity).add(resource);
        } else {
            List<URI> resources = new ArrayList<URI>();
            resources.add(resource);
            linkedActivities.put(activity, resources);
        }
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

}
