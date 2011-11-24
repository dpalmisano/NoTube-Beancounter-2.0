package tv.notube.usermanager.services.auth;

import tv.notube.commons.model.User;
import tv.notube.usermanager.services.auth.oauth.OAuthToken;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface AuthHandler {

    public User auth(User user, String token) throws AuthHandlerException;

    /**
     * To handle <i>OAuth</i> callbacks.
     *
     * @param user
     * @param token
     * @param verifier
     * @return
     * @throws AuthHandlerException
     */
    public User auth(
            User user,
            String token,
            String verifier
    ) throws AuthHandlerException;

    public OAuthToken getToken(String username) throws AuthHandlerException;
}
