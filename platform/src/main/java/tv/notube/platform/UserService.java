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
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserService extends Service {

    @InjectParam
    private InstanceManager instanceManager;

    @OPTIONS
    @Path("/register")
    public Response corsSignUp(
            @HeaderParam("Access-Control-Request-Headers") String requestH
    ) {
        _corsHeaders = requestH;
        return makeCORS(Response.ok(), requestH);
    }

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
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Your application is not authorized.Sorry.")
            );
            return makeCORS(rb);
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
                    "Error while getting application with key '" + apiKey + "'",
                    e
            );
        }
        if (application == null) {
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
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "user successfully registered",
                user.getId())
        );
        return makeCORS(rb);
    }

    @OPTIONS
    @Path("/{username}")
    public Response corsGetUser(
            @HeaderParam("Access-Control-Request-Headers") String requestH
    ) {
        _corsHeaders = requestH;
        return makeCORS(Response.ok(), requestH);
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
                    "Error while authenticating your application",
                    e
            );
        }
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Sorry. You're not allowed to do that.")
            );
            return makeCORS(rb);
        }

        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            final String errMsg = "Error while getting user '" + username + "'.";
            throw new RuntimeException(errMsg);
        }
        if (user == null) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "user '" + username + "' not found",
                    null)
            );
            return makeCORS(rb);
        }
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "user '" + username + "' found",
                user)
        );
        return makeCORS(rb);
    }

    @OPTIONS
    @Path("activities/{username}")
    public Response corsGetActivities(
            @HeaderParam("Access-Control-Request-Headers") String requestH
    ) {
        _corsHeaders = requestH;
        return makeCORS(Response.ok(), requestH);
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
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Sorry. You're not allowed to do that.")
            );
            return makeCORS(rb);
        }
        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while retrieving user '" + username + "'", e);
        }
        if (user == null) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(
                    new PlatformResponse(
                            PlatformResponse.Status.NOK,
                            "user with username '" + username + "' not found")
            );
            return makeCORS(rb);
        }
        List<Activity> activities;
        try {
            activities = um.getUserActivities(user.getId());
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while getting user '" + username
                    + "' activities", e);
        }
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "user '" + username + "' activities found",
                activities)
        );
        return makeCORS(rb);
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
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(
                    new PlatformResponse(
                            PlatformResponse.Status.NOK,
                            "user with username '" + username + "' not found")
            );
            return makeCORS(rb);
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
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Sorry, you're not allowed to do that")
            );
            return makeCORS(rb);
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
        Response.ResponseBuilder rb = Response.serverError();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "user with username '" + username + "' deleted")
        );
        return makeCORS(rb);
    }

    @OPTIONS
    @Path("authenticate/{username}")
    public Response corsAuthenticate(
            @HeaderParam("Access-Control-Request-Headers") String requestH
    ) {
        _corsHeaders = requestH;
        return makeCORS(Response.ok(), requestH);
    }

    @POST
    @Path("authenticate/{username}")
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
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Sorry, you're not allowed to do that")
            );
            return makeCORS(rb);
        }

        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while retrieving user '" + username + "'", e);
        }
        if (user == null) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "user with username '" + username + "' not found")
            );
            return makeCORS(rb);
        }
        if (!user.getPassword().equals(password)) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "password for '" + username + "' incorrect")
            );
            return makeCORS(rb);
        }
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "user '" + username + "' authenticated")
        );
        return makeCORS(rb);
    }

    @GET
    @Path("/oauth/token/{service}/{username}")
    public Response getOAuthToken(
            @PathParam("service") String service,
            @PathParam("username") String username,
            @QueryParam("redirect") String finalRedirect
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
        URL finalRedirectUrl;
        try {
            finalRedirectUrl = new URL(finalRedirect);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Error while getting token for user '" + username + "' " +
                            "on service '" + service + "'",
                    e
            );
        }
        try {
            um.setUserFinalRedirect(userObj.getUsername(), finalRedirectUrl);
        } catch (UserManagerException e) {
            throw new RuntimeException(
                    "Error while setting temporary final redirect URL " +
                            "for user '" + username + "' " + "on service '" + service + "'",
                    e
            );
        }
        URL redirect = oAuthToken.getRedirectPage();
        try {
            return Response.temporaryRedirect(redirect.toURI()).build();
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
    public Response handleOAuthCallback(
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
        URL finalRedirectUrl;
        try {
            finalRedirectUrl = um.consumeUserFinalRedirect(userObj.getUsername());
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while getting final redirect " +
                    "URL for user '" + username + "' for service '" + service + "'",
                    e);
        }
        try {
            return Response.temporaryRedirect(finalRedirectUrl.toURI()).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Malformed redirect URL", e);
        }
    }

    @GET
    @Path("/auth/callback/{service}/{username}/{redirect}")
    public Response handleAuthCallback(
            @PathParam("service") String service,
            @PathParam("username") String username,
            @PathParam("redirect") String redirect,
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
        URL finalRedirectUrl;
        try {
            finalRedirectUrl = new URL(
                    "http://" + URLDecoder.decode(redirect, "UTF-8")
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Error while getting token for user '" + username + "' " +
                            "on service '" + service + "'",
                    e
            );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(
                    "Error while getting token for user '" + username + "' " +
                            "on service '" + service + "'",
                    e
            );
        }
        try {
            return Response.temporaryRedirect(finalRedirectUrl.toURI()).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Malformed redirect URL", e);
        }
    }

    @OPTIONS
    @Path("source/{username}/{service}")
    public Response corsRemoveSource(
            @HeaderParam("Access-Control-Request-Headers") String requestH
    ) {
        _corsHeaders = requestH;
        return makeCORS(Response.ok(), requestH);
    }

    @DELETE
    @Path("source/{username}/{service}")
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
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "You're not allow to do that. Sorry.")
            );
            return makeCORS(rb);
        }

        try {
            um.deregisterService(service, userObj);
        } catch (UserManagerException e) {
            throw new RuntimeException("Error while retrieving user '" + username + "'", e);
        }
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "service '" + service + "' removed from user '" + username + "'")
        );
        return makeCORS(rb);
    }

    @OPTIONS
    @Path("profile/{username}")
    public Response corsGetProfile(
            @HeaderParam("Access-Control-Request-Headers") String requestH
    ) {
        _corsHeaders = requestH;
        return makeCORS(Response.ok(), requestH);
    }

    @GET
    @Path("profile/{username}")
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
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Sorry. You're not allowed to do that.")
            );
            return makeCORS(rb);
        }
        ProfileStore ps = instanceManager.getProfileStore();
        UserProfile up;
        try {
            up = ps.getUserProfile(username);
        } catch (ProfileStoreException e) {
            throw new RuntimeException("Error while retrieving profile for user '" + username + "'", e);
        }
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "profile for user '" + username + "' found",
                up
        )
        );
        return makeCORS(rb);
    }

}
