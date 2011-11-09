package tv.notube.commons.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * It's the main class representative of a {@link User} profile.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UserProfile extends Referenceable {

    public enum Visibility {
        PRIVATE,
        PROTECTED,
        PUBLIC
    }

    private Visibility visibility;

    private String username;

    private List<Interest> interests = new ArrayList<Interest>();

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public void addInterest(Interest interest) {
        this.interests.add(interest);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "visibility=" + visibility +
                ", username='" + username + '\'' +
                ", interests=" + interests +
                "} " + super.toString();
    }
}
