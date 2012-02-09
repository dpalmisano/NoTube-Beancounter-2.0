package tv.notube.commons.model.auth;

import com.google.gson.annotations.Expose;
import tv.notube.commons.model.auth.Auth;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SimpleAuth extends Auth {

    @Expose
    private String username;

    public SimpleAuth(String session, String username) {
        super(session);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "SimpleAuth{" +
                "username='" + username + '\'' +
                "} " + super.toString();
    }
}
