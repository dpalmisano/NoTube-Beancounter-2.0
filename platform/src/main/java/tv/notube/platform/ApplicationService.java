package tv.notube.platform;

import com.sun.jersey.api.core.InjectParam;
import tv.notube.applications.Application;
import tv.notube.applications.ApplicationsManager;
import tv.notube.applications.ApplicationsManagerException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Path("/application")
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationService {

    @InjectParam
    private InstanceManager instanceManager;

    @POST
    @Path("/register")
    public PlatformResponse register(
            @FormParam("name") String name,
            @FormParam("description") String description,
            @FormParam("email") String email,
            @FormParam("oauthCallback") String oauth
    ) {
        ApplicationsManager am = instanceManager.getApplicationManager();
        Application application = new Application(name, description, email);
        try {
            application.setOAuthCallback(new URL(oauth));
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Provided URL '" + oauth + "' is not well formed",
                    e
            );
        }
        String apiKey;
        try {
            apiKey = am.registerApplication(application);
        } catch (ApplicationsManagerException e) {
            throw new RuntimeException(
                    "Error while registering application '" + name + "'",
                    e
            );
        }
        return new PlatformResponse(
                PlatformResponse.Status.OK,
                "Application '" + name + "' successfully registered",
                apiKey
        );
    }

    @DELETE
    @Path("/{name}")
    public PlatformResponse deregisterApplication(
            @PathParam("name") String name
    ) {
        ApplicationsManager am = instanceManager.getApplicationManager();
        try {
            am.deregisterApplication(name);
        } catch (ApplicationsManagerException e) {
            throw new RuntimeException(
                    "Error while deregistering application '" + name + "'",
                    e
            );
        }
        return new PlatformResponse(
                PlatformResponse.Status.OK,
                "Application '" + name + "' successfully removed"
        );
    }
}
