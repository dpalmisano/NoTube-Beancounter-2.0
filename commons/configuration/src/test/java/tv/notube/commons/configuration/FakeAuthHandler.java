package tv.notube.commons.configuration;

import tv.notube.commons.model.*;
import tv.notube.commons.model.auth.AuthHandlerException;
import tv.notube.commons.model.auth.DefaultAuthHandler;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class FakeAuthHandler extends DefaultAuthHandler {

    public FakeAuthHandler(Service service) {
        super(service);
    }

    public User auth(User user, String token) throws AuthHandlerException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public User auth(User user, String token, String verifier) throws AuthHandlerException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public OAuthToken getToken(String username) throws AuthHandlerException {
        throw new UnsupportedOperationException("Not implemented");
    }
}
