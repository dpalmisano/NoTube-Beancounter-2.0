package tv.notube.platform;

import com.sun.jersey.api.core.InjectParam;
import tv.notube.applications.Application;
import tv.notube.applications.ApplicationsManager;
import tv.notube.applications.ApplicationsManagerException;
import tv.notube.applications.Permission;
import tv.notube.commons.model.User;
import tv.notube.commons.model.UserProfile;
import tv.notube.commons.model.activity.Activity;
import tv.notube.platform.utils.ParametersUtil;
import tv.notube.profiler.storage.ProfileStore;
import tv.notube.profiler.storage.ProfileStoreException;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerException;
import tv.notube.usermanager.services.auth.oauth.OAuthToken;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {

    @InjectParam
    private InstanceManager instanceManager;

    @POST
    @Path("/register")
    public Response signUp(
            @FormParam("name") String name,
            @FormParam("surname") String surname,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @QueryParam("apikey") String apiKey
    ) {
        ParametersUtil.check(name, surname, username, password, apiKey);

        ApplicationsManager am = instanceManager.getApplicationManager();
        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            throw new RuntimeException(
                    "Error while authorizing your application",
                    e
            );
        }
        if(!isAuth) {
            return new Response(
                    Response.Status.NOK,
                    "Your application is not authorized.Sorry."
            );
        }
        UserManager um = instanceManager.getUserManager();
        try {
            if (um.getUser(username) != null) {
                final String errMsg = "username '" + username + "' is already taken";
                throw new RuntimeException(errMsg);
            }
        } catch (UserManagerException e) {
            final String errMsg = "Error while calling the UserManager";
            throw new RuntimeException(errMsg, e);
        }

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setPassword(password);
        try {
            um.storeUser(user);
        } catch (UserManagerException e) {
            final String errMsg = "Error while storing user '" + user + "'.";
            throw new RuntimeException(errMsg);
        }

        Application application;
        try {
            application = am.getApplicationByApiKey(apiKey);
        } catch (ApplicationsManagerException e) {
            throw new RuntimeException(
                    "Error while getting application with key '" + apiKey + "'" ,
                    e
            );
        }
        if(application == null) {
            throw new RuntimeException("Application not found");
        }

        try {
            am.grantPermission(
                    application.getName(),
                    user.getId(),
                    Permission.Action.DELETE
            );
            am.grantPermission(
                    application.getName(),
                    user.getId(),
                    Permission.Action.UPDATE
            );
        } catch (ApplicationsManagerException e) {
            throw new RuntimeException(
                    "Error while granting permissions on user " + user.getId(),
                    e
            );
        }

        return new Response(
                Response.Status.OK,
                "user successfully registered",
                user.getId()
        );
    }

    @GET
    @Path("/{username}")
    public Response getUser(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey
    ) {
        ParametersUtil.check(username, apiKey);
        UserManager um = instanceManager.getUserManager();
        ApplicationsManager am = instanceManager.getApplicationManager();

        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            throw new RuntimeException(
                    "Error while authenticating you application",
                    e
            );
        }

        if(!isAuth) {
            return new Response(
                    Response.Status.NOK,
                    "Sorry. You're not allowed to do that."
            );
        }

        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            final String errMsg = "Error while getting user '" + username + "'.";
            throw new RuntimeException(errMsg);
        }
        if (user == null) {
            return new Response(
                    Response.Status.NOK,
                    "user '" + username + "' not found",
                    null
            );
        }
        return new Response(
                Response.Status.OK,
                "user '" + username + "' found",
                user
        );
    }

    @GET
    @Path("activities/{username}")
    public Response getActivities(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey
    ) {
        ParametersUtil.check(username, apiKey);

        UserManager um = instanceManager.getUserManager();

        ApplicationsManager am = instanceManager.getApplicationManager();

        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            throw new RuntimeException(
                    "Error while authenticating you application",
                    e
            );
        }

        if(!isAuth) {
            return new Response(
                    Response.Status.NOK,
                    "Sorry. You're not allowed to do that."
            );
        }

        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while retrieving user '" + username + "'", e);
        }
        if (user == null) {
            return new Response(
                    Response.Status.NOK,
                    "user with username '" + username + "' not found"
            );
        }
        List<Activity> activities;
        try {
            activities = um.getUserActivities(user.getId());
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while getting user '" + username
                    + "' activities", e);
        }
        return new Response(
                Response.Status.OK,
                "user '" + username + "' activities found",
                activities
        );
    }

    @DELETE
    @Path("/{username}")
    public Response deleteUser(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey
    ) {
        ParametersUtil.check(username, apiKey);

        UserManager um = instanceManager.getUserManager();
        ProfileStore ps = instanceManager.getProfileStore();
        ApplicationsManager am = instanceManager.getApplicationManager();

        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while retrieving user '" + username + "'", e);
        }
        if (user == null) {
            return new Response(
                    Response.Status.NOK,
                    "user with username '" + username + "' not found"
            );
        }

        boolean isAuth;
        try {
            isAuth = am.isAuthorized(
                    apiKey,
                    user.getId(),
                    Permission.Action.DELETE
            );
        } catch (ApplicationsManagerException e) {
            throw new RuntimeException(
                    "Error while authorizing your application",
                    e
            );
        }
        if(!isAuth) {
            return new Response(
                    Response.Status.NOK,
                    "Sorry, you're not allowed to do that"
            );
        }

        try {
            um.deleteUser(user.getId());
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while deleting user '" + username
                    + "'", e);
        }
        try {
            ps.deleteUserProfile(username);
        } catch (ProfileStoreException e) {
            throw new RuntimeException("Error while deleting user '" + username +
                    "'");
        }
        return new Response(
                Response.Status.OK,
                "user with username '" + username + "' not found"
        );
    }

    @POST
    @Path("/authenticate/{username}")
    public Response authenticate(
            @PathParam("username") String username,
            @FormParam("password") String password,
            @QueryParam("apikey") String apiKey
    ) {
        ParametersUtil.check(username, password, apiKey);
        UserManager um = instanceManager.getUserManager();

        ApplicationsManager am = instanceManager.getApplicationManager();

        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            throw new RuntimeException(
                    "Error while authenticating your application",
                    e
            );
        }

        if(!isAuth) {
            return new Response(
                    Response.Status.NOK,
                    "Sorry. You're not allowed to do that."
            );
        }

        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while retrieving user '" + username + "'", e);
        }
        if (user == null) {
            return new Response(
                    Response.Status.NOK,
                    "user with username '" + username + "' not found"
            );
        }
        if (!user.getPassword().equals(password)) {
            return new Response(
                    Response.Status.NOK,
                    "password for '" + username + "' incorrect"
            );
        }
        return new Response(
                Response.Status.OK,
                "user '" + username + "' authenticated"
        );
    }

    @GET
    @Path("/oauth/token/{service}/{username}")
    public javax.ws.rs.core.Response getOAuthToken(
            @PathParam("service") String service,
            @PathParam("username") String username
    ) {
        UserManager um = instanceManager.getUserManager();
        User userObj;
        try {
            userObj = um.getUser(username);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while retrieving user '" + username + "'", e);
        }
        OAuthToken oAuthToken;
        try {
            oAuthToken = um.getOAuthToken(service, userObj.getUsername());
        } catch (UserManagerException e) {
            throw new RuntimeException(
                    "Error while getting token for user '" + username + "' " +
                            "on service '" + service + "'",
                    e
            );
        }
        URL redirect = oAuthToken.getRedirectPage();
        try {
            return javax.ws.rs.core.Response.temporaryRedirect(redirect.toURI()).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Malformed redirect URL", e);
        }
    }

    @GET
    @Path("/oauth/callback/facebook/{username}/")
    public Response handleFacebookAuthCallback(
            @PathParam("username") String username,
            @QueryParam("code") String verifier
    ) {
        // Facebook OAuth exchange quite different from Twitter's one.
        ParametersUtil.check(username, verifier);
        return handleAuthCallback("facebook", username, null, verifier);
    }

    @GET
    @Path("/oauth/callback/{service}/{username}/")
    public Response handleAuthCallback(
            @PathParam("service") String service,
            @PathParam("username") String username,
            @QueryParam("oauth_token") String token,
            @QueryParam("oauth_verifier") String verifier
    ) {
        UserManager um = instanceManager.getUserManager();
        User userObj;
        try {
            userObj = um.getUser(username);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while retrieving user '" + username + "'", e);
        }
        try {
            um.registerOAuthService(service, userObj, token, verifier);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while OAuth-like exchange for " +
                    "service: '" + service + "'", e);
        }
        return new Response(
                Response.Status.OK,
                "service '" + service + " as been successfully added to user '" + username + "'"
        );
    }

    @GET
    @Path("/callback/{service}/{username}/")
    public Response handleAuthCallback(
            @PathParam("service") String service,
            @PathParam("username") String username,
            @QueryParam("token") String token
    ) {
        UserManager um = instanceManager.getUserManager();
        User userObj;
        try {
            userObj = um.getUser(username);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while retrieving user '" + username + "'", e);
        }
        try {
            um.registerService(service, userObj, token);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while OAuth-like exchange for service: '" + service + "'");
        }
        return new Response(
                Response.Status.OK,
                "service '" + service + " as been successfully added to user '" + username + "'",
                null
        );
    }

    @DELETE
    @Path("/source/{username}/{service}")
    public Response removeSource(
            @PathParam("username") String username,
            @PathParam("service") String service,
            @QueryParam("apikey") String apiKey
    ) {
        ParametersUtil.check(username, service, apiKey);

        UserManager um = instanceManager.getUserManager();
        User userObj;
        try {
            userObj = um.getUser(username);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while retrieving user '" + username + "'", e);
        }

        ApplicationsManager am = instanceManager.getApplicationManager();

        boolean isAuth;
        try {
            isAuth = am.isAuthorized(
                    apiKey,
                    userObj.getId(),
                    Permission.Action.UPDATE
            );
        } catch (ApplicationsManagerException e) {
            throw new RuntimeException(
                    "Error while asking for permissions",
                    e
            );
        }
        if(!isAuth) {
            return new Response(
                    Response.Status.NOK,
                    "You're not allow to do that. Sorry."
            );
        }

        try {
            um.deregisterService(service, userObj);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while retrieving user '" + username + "'", e);
        }
        return new Response(
                Response.Status.OK,
                "service '" + service + "' removed from user '" + username + "'"
        );
    }

    @GET
    @Path("/profile/{username}")
    public Response getProfile(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey
    ) {
        ParametersUtil.check(username, apiKey);

        ApplicationsManager am = instanceManager.getApplicationManager();

        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            throw new RuntimeException(
                    "Error while authenticating you application",
                    e
            );
        }

        if(!isAuth) {
            return new Response(
                    Response.Status.NOK,
                    "Sorry. You're not allowed to do that."
            );
        }

        ProfileStore ps = instanceManager.getProfileStore();
        UserProfile up;
        try {
            up = ps.getUserProfile(username);
        } catch (ProfileStoreException e) {
            throw new RuntimeException("Error while retrieving profile for user '" + username + "'", e);
        }
        return new Response(
                Response.Status.OK,
                "profile for user '" + username + "' found",
                up
        );
    }

}
