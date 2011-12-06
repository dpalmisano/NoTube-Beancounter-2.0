package tv.notube.platform.jersey;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import org.apache.log4j.Logger;

public class JerseyRESTFrontend implements RESTFrontend {

    private RESTFrontendConfig config;
    private SelectorThread selectorThread;
    private Logger logger = Logger.getLogger(JerseyRESTFrontend.class);

    protected JerseyRESTFrontend(RESTFrontendConfig config) {
        if(config == null) {
            throw new NullPointerException("config cannot be null.");
        }
        this.config = config;
    }

    public void startService() {

          try {
            if (selectorThread == null) {
                selectorThread = GrizzlyWebContainerFactory.create(config.getURI(), config.getParams());
            } else {
                selectorThread.startEndpoint();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while starting service.", e);
        }
    }

    public void stopService() {
        if(selectorThread != null) {
            selectorThread.stopEndpoint();
        }
    }
}
