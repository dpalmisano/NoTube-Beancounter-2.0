package tv.notube.commons.model;

import tv.notube.commons.model.activity.Activity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * It represents a {@link User} interest.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Interest extends Referenceable {

    private boolean visible;

    private float weight;

    private List<Activity> activities = new ArrayList<Activity>();

    public void setResource(URI resource) {
        this.reference = resource;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public boolean addActivity(Activity activity) {
        return this.activities.add(activity);
    }

    @Override
    public String toString() {
        return "Interest{" +
                "visible=" + visible +
                ", weight=" + weight +
                ", activities=" + activities +
                "} " + super.toString();
    }
}
