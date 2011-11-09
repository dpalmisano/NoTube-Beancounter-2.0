package tv.notube.extension.profilingline;

import tv.notube.commons.model.activity.Activity;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UnweightedInterests {

    private String user;

    private Map<URI, List<Activity>> interests;

    public UnweightedInterests(String user, Map<URI, List<Activity>> interests) {
        this.user = user;
        this.interests = interests;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Map<URI, List<Activity>> getInterests() {
        return interests;
    }

    public void setInterests(Map<URI, List<Activity>> interests) {
        this.interests = interests;
    }
}
