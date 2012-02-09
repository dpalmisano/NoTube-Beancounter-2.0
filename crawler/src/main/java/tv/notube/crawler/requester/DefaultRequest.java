package tv.notube.crawler.requester;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import tv.notube.commons.model.auth.Auth;
import tv.notube.commons.model.Service;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class DefaultRequest implements Request {

    protected Auth auth;

    protected Service service;

    protected HttpClient httpClient;

    public DefaultRequest() {
        httpClient = new DefaultHttpClient();
    }

    public void setAuth(Auth auth) throws RequestException {
        this.auth = auth;
    }

    public void setService(Service service) throws RequestException {
        this.service = service;
    }

}
