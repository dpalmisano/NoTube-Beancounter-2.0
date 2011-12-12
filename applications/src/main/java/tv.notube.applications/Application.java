package tv.notube.applications;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Application implements Serializable {

    static final long serialVersionUID = 13211239608137490L;

    private String name;

    private String description;

    private String email;

    private URL callback;

    private URL website;

    private String apiKey;

    private Map<UUID, Permission> permissions =
            new HashMap<UUID, Permission>();

    public Application(String name, String description, String email) {
        this.name = name;
        this.description = description;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public void setOAuthCallback(URL callback) {
        this.callback = callback;
    }

    public URL getCallback() {
        return callback;
    }

    public URL getWebsite() {
        return website;
    }

    public void setWebsite(URL website) {
        this.website = website;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void addPermission(Permission permission) {
        this.permissions.put(permission.getResource(), permission);
    }

    public Permission getPermission(UUID resource) {
        return permissions.get(resource);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Application that = (Application) o;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }


    @Override
    public String toString() {
        return "Application{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", callback=" + callback +
                ", website=" + website +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }

}
