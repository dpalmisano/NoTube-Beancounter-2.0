package tv.notube.commons.model.auth;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class Auth implements Serializable {

    private static final long serialVersionUID = 11251145235L;

    @Expose
    private String session;

    public Auth(String session) {
        this.session = session;
    }

    public String getSession() {
        return session;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "session='" + session + '\'' +
                '}';
    }
}
