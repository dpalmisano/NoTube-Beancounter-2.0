package tv.notube.usermanager.services.auth;

import tv.notube.commons.model.User;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface AuthHandler {

    public User auth(User user, String token) throws AuthHandlerException;

}
