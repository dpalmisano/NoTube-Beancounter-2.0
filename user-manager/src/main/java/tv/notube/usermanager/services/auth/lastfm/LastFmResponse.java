package tv.notube.usermanager.services.auth.lastfm;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LastFmResponse {

    private String name;

    private String key;

    public LastFmResponse(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "LastFmResponse{" +
                "name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
