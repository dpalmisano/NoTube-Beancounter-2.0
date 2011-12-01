package tv.notube.commons.storage.alog.jersey;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class JerseyRESTFrontendConfig implements RESTFrontendConfig {

    private URI uri;

    private static HashMap<String, String> params;

    static {
        params = new HashMap<String,String>();
        //tells which package to scan for resources
        params.put("com.sun.jersey.config.property.packages",
                "tv.notube.commons.storage.alog");
    }

    public JerseyRESTFrontendConfig(URI base_uri) {
        if(base_uri == null) {
            throw new NullPointerException("base_uri cannot be null.");
        }
        this.uri = base_uri;
        this.params = new HashMap<String,String>(params);
    }

    public URI getURI() {
        return uri;
    }

    public Map<String, String> getParams() {
        return params;
    }

}
