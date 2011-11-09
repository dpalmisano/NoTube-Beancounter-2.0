package tv.notube.commons.model;

import java.io.Serializable;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Auth implements Serializable {

    private static final long serialVersionUID = 11251145235L;

    private String session;

    private String username;

    public Auth(String session, String username) {
        this.session = session;
        this.username = username;
    }

    public String getSession() {
        return session;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "session='" + session + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
