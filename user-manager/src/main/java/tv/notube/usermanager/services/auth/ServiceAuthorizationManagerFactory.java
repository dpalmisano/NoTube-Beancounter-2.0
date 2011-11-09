package tv.notube.usermanager.services.auth;

import tv.notube.commons.model.Service;
import tv.notube.usermanager.services.auth.lastfm.LastFmAuthHandler;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 *
 */
public class ServiceAuthorizationManagerFactory {

    private static ServiceAuthorizationManagerFactory instance =
            new ServiceAuthorizationManagerFactory();

    public static ServiceAuthorizationManagerFactory getInstance() {
        return instance;
    }

    private ServiceAuthorizationManager sam;

    private ServiceAuthorizationManagerFactory() {
        sam = new DefaultServiceAuthorizationManager();

        Service lastfm = new Service();
        lastfm.setName("lastfm");
        lastfm.setDescription("Provides access to Lastfm user data");
        lastfm.setApikey("9f57b916d7ab90a7bf562b9e6f2385f0");
        lastfm.setSecret("c57210237463dc19277b840727b7f11d");
        lastfm.setSessionEndpoint("http://ws.audioscrobbler.com/2.0/?");
        try {
            lastfm.setEndpoint(new URL("http://ws.audioscrobbler.com/2.0/?"));
        } catch (MalformedURLException e) {
            // cannot be
        }
        try {
            sam.addHandler(lastfm, new LastFmAuthHandler(lastfm));
        } catch (ServiceAuthorizationManagerException e) {
            throw new RuntimeException("Error while registering handler", e);
        }
    }

    public ServiceAuthorizationManager build() {
        return sam;
    }

}
