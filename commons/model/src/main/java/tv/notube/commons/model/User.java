package tv.notube.commons.model;

import org.joda.time.DateTime;
import tv.notube.commons.model.activity.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models the main NoTube user characteristics.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class User extends Referenceable implements Serializable {

    private static final long serialVersionUID = 324345235L;

    private String name;

    private String surname;

    private DateTime profiledAt;

    private boolean forcedProfiling;

    private List<Activity> activities = new ArrayList<Activity>();

    private Map<String, Auth> services = new HashMap<String, Auth>();

    private String password;

    private String username;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public DateTime getProfiledAt() {
        return profiledAt;
    }

    public void setProfiledAt(DateTime profiledAt) {
        this.profiledAt = profiledAt;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isForcedProfiling() {
        return forcedProfiling;
    }

    public void setForcedProfiling(boolean forcedProfiling) {
        this.forcedProfiling = forcedProfiling;
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

    public List<String> getServices() {
        return new ArrayList<String>(services.keySet());
    }

    public Auth getAuth(String service) {
        return services.get(service);
    }

    public void addService(String service, Auth auth) {
        services.put(service, auth);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", profiledAt=" + profiledAt +
                ", forcedProfiling=" + forcedProfiling +
                ", activities=" + activities +
                ", services=" + services +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                "} " + super.toString();
    }
}
