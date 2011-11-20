package tv.notube.extension.profilingline;

import tv.notube.commons.model.Interest;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class WeightedInterests {

    private String user;

    private Set<Interest> interests = new HashSet<Interest>();

    public WeightedInterests(String user, Set<Interest> interests) {
        this.user = user;
        this.interests = interests;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }
}
