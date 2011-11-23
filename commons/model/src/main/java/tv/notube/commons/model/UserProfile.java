package tv.notube.commons.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private List<Type> types = new ArrayList<Type>();

    private Set<Interest> interests = new HashSet<Interest>();

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

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void addType(Type type) {
        this.types.add(type);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "visibility=" + visibility +
                ", username='" + username + '\'' +
                ", types=" + types +
                ", interests=" + interests +
                "} " + super.toString();
    }
}
