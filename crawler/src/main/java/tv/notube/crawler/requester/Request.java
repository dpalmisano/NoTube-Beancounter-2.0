package tv.notube.crawler.requester;

import tv.notube.commons.model.auth.Auth;
import tv.notube.commons.model.Service;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Request {

    /**
     *
     * @throws RequestException
     */
    public void setAuth(Auth auth)
        throws RequestException;

    /**
     *
     * @return
     * @throws RequestException
     */
    public ServiceResponse call()
            throws RequestException;

    /**
     *
     * @param service
     * @throws RequestException
     */
    void setService(Service service)
            throws RequestException;
}
