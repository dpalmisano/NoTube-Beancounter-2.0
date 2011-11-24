package tv.notube.usermanager.services.auth.twitter;

import tv.notube.usermanager.services.auth.oauth.OAuthToken;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TwitterOAuthResponse {

    private Status status;

    private OAuthToken oAuthToken;

    public enum Status {
        OK,
        NOK
    }

    public TwitterOAuthResponse(Status status) {
        this.status = status;
    }

    public TwitterOAuthResponse(OAuthToken oAuthToken) {
        this.status = Status.OK;
        this.oAuthToken = oAuthToken;
    }

    public OAuthToken getOAuthToken() {
        return oAuthToken;
    }
}
