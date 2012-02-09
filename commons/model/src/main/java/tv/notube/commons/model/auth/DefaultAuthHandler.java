package tv.notube.commons.model.auth;

import tv.notube.commons.model.Service;
import tv.notube.commons.model.auth.AuthHandler;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class DefaultAuthHandler implements AuthHandler {

    protected Service service;

    public DefaultAuthHandler(Service service) {
        this.service = service;
    }

}
