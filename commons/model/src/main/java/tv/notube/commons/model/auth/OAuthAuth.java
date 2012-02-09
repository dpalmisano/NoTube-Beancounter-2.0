package tv.notube.commons.model.auth;

import com.google.gson.annotations.Expose;
import tv.notube.commons.model.auth.Auth;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class OAuthAuth extends Auth {

    @Expose
    private String secret;

    public OAuthAuth(String session, String secret) {
        super(session);
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public String toString() {
        return "OAuthAuth{" +
                "secret='" + secret + '\'' +
                "} " + super.toString();
    }
}
