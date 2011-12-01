package tv.notube.commons.storage.alog.jersey;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class JerseyRESTFrontendFactory {

    private static JerseyRESTFrontendFactory instance;

    public static JerseyRESTFrontendFactory getInstance() {
        if (instance == null) {
            instance = new JerseyRESTFrontendFactory();
        }
        return instance;
    }

    private JerseyRESTFrontendFactory() {}

    public RESTFrontend create(RESTFrontendConfig conf) {
        if(conf == null) {
            throw new NullPointerException("conf cannot be null.");
        }
        return new JerseyRESTFrontend(conf);
    }

}
