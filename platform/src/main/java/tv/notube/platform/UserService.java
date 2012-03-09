package tv.notube.platform;

import com.sun.jersey.api.core.InjectParam;
import tv.notube.applications.Application;
import tv.notube.applications.ApplicationsManager;
import tv.notube.applications.ApplicationsManagerException;
import tv.notube.applications.Permission;
import tv.notube.commons.model.OAuthToken;
import tv.notube.commons.model.User;
import tv.notube.commons.model.UserProfile;
import tv.notube.commons.model.activity.Activity;
import tv.notube.crawler.Crawler;
import tv.notube.crawler.CrawlerException;
import tv.notube.crawler.Report;
import tv.notube.profiler.storage.ProfileStore;
import tv.notube.profiler.storage.ProfileStoreException;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerException;

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
public class UserService extends JsonService {

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
        try {
            check(
                    this.getClass(),
                    "signUp",
                    name,
                    surname,
                    username,
                    password,
                    apiKey
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters");
        }
        ApplicationsManager am = instanceManager.getApplicationManager();
        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authorizing your application");
        }
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Your application is not authorized.Sorry.")
            );
            return rb.build();
        }
        UserManager um = instanceManager.getUserManager();
        try {
            if (um.getUser(username) != null) {
                final String errMsg = "username '" + username + "' is already taken";
                Response.ResponseBuilder rb = Response.serverError();
                rb.entity(new PlatformResponse(
                        PlatformResponse.Status.NOK,
                        errMsg)
                );
                return rb.build();
            }
        } catch (UserManagerException e) {
            final String errMsg = "Error while calling the UserManager";
            return error(e, errMsg);
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
            return error(e, errMsg);
        }

        Application application;
        try {
            application = am.getApplicationByApiKey(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while getting application with key '" + apiKey + "'");
        }
        if (application == null) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Application not found")
            );
            return rb.build();
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
            return error(e, "Error while granting permissions on user " +  user.getId());
        }
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "user successfully registered",
                user.getId())
        );
        return rb.build();
    }

    @GET
    @Path("/{username}")
    public Response getUser(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey
    ) {
        try {
            check(
                    this.getClass(),
                    "getUser",
                    username,
                    apiKey
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters");
        }
        UserManager um = instanceManager.getUserManager();
        ApplicationsManager am = instanceManager.getApplicationManager();
        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authenticating your application");
        }
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Sorry. You're not allowed to do that.")
            );
            return rb.build();
        }

        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            final String errMsg = "Error while getting user '" + username + "'.";
            return error(e, errMsg);
        }
        if (user == null) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "user '" + username + "' not found",
                    null)
            );
            return rb.build();
        }

        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "user '" + username + "' found",
                user)
        );
        return rb.build();
    }

    @GET
    @Path("activities/{username}")
    public Response getActivities(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey
    ) {
        try {
            check(
                    this.getClass(),
                    "getActivities",
                    username,
                    apiKey
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters");
        }
        UserManager um = instanceManager.getUserManager();
        ApplicationsManager am = instanceManager.getApplicationManager();

        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authenticating you application");
        }
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Sorry. You're not allowed to do that.")
            );
            return rb.build();
        }
        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            return error(e, "Error while retrieving user '" + username + "'");
        }
        if (user == null) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(
                    new PlatformResponse(
                            PlatformResponse.Status.NOK,
                            "user with username '" + username + "' not found")
            );
            return rb.build();
        }
        List<Activity> activities;
        try {
            activities = um.getUserActivities(user.getId());
        } catch (UserManagerException e) {
            return error(e, "Error while getting user '" + username + "' activities");
        }
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "user '" + username + "' activities found",
                activities)
        );
        return rb.build();
    }

    @DELETE
    @Path("/{username}")
    public Response deleteUser(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey
    ) {
        try {
            check(
                    this.getClass(),
                    "deleteUser",
                    username,
                    apiKey
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters");
        }
        UserManager um = instanceManager.getUserManager();
        ProfileStore ps = instanceManager.getProfileStore();
        ApplicationsManager am = instanceManager.getApplicationManager();

        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            return error(e, "Error while retrieving user '" + username + "'");
        }
        if (user == null) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(
                    new PlatformResponse(
                            PlatformResponse.Status.NOK,
                            "user with username '" + username + "' not found")
            );
            return rb.build();
        }

        boolean isAuth;
        try {
            isAuth = am.isAuthorized(
                    apiKey,
                    user.getId(),
                    Permission.Action.DELETE
            );
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authorizing your application");
        }
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Sorry, you're not allowed to do that")
            );
            return rb.build();
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
            return error(e, "Error while deleting user '" + username + "'");
        }
        Response.ResponseBuilder rb = Response.serverError();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "user with username '" + username + "' deleted")
        );
        return rb.build();
    }

    @POST
    @Path("authenticate/{username}")
    public Response authenticate(
            @PathParam("username") String username,
            @FormParam("password") String password,
            @QueryParam("apikey") String apiKey
    ) {
        try {
            check(
                    this.getClass(),
                    "authenticate",
                    username,
                    password,
                    apiKey
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters");
        }
        UserManager um = instanceManager.getUserManager();
        ApplicationsManager am = instanceManager.getApplicationManager();
        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authenticating your application");
        }
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Sorry, you're not allowed to do that")
            );
            return rb.build();
        }
        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            return error(e, "Error while retrieving user '" + username + "'");
        }
        if (user == null) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "user with username '" + username + "' not found")
            );
            return rb.build();
        }
        if (!user.getPassword().equals(password)) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "password for '" + username + "' incorrect")
            );
            return rb.build();
        }
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "user '" + username + "' authenticated")
        );
        return rb.build();
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
            return error(e, "Error while retrieving user '" + username + "'");
        }
        OAuthToken oAuthToken;
        try {
            oAuthToken = um.getOAuthToken(service, userObj.getUsername());
        } catch (UserManagerException e) {
            return error(e, "Error while getting token for user '" + username + "' " +
                            "on service '" + service + "'");
        }
        URL finalRedirectUrl;
        try {
            finalRedirectUrl = new URL(finalRedirect);
        } catch (MalformedURLException e) {
            return error(e, "Error while getting token for user '" + username + "' " +
                            "on service '" + service + "'");
        }
        try {
            um.setUserFinalRedirect(userObj.getUsername(), finalRedirectUrl);
        } catch (UserManagerException e) {
            return error(e, "Error while setting temporary final redirect URL " +
                            "for user '" + username + "' " + "on service '" + service + "'");
        }
        URL redirect = oAuthToken.getRedirectPage();
        try {
            return Response.temporaryRedirect(redirect.toURI()).build();
        } catch (URISyntaxException e) {
            return error(e, "Malformed redirect URL");
        }
    }

    @GET
    @Path("/oauth/callback/facebook/{username}/")
    public Response handleFacebookAuthCallback(
            @PathParam("username") String username,
            @QueryParam("code") String verifier
    ) {
        // Facebook OAuth exchange quite different from Twitter's one.
        return handleOAuthCallback("facebook", username, null, verifier);
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
            return error(e, "Error while retrieving user '" + username + "'");
        }
        try {
            um.registerOAuthService(service, userObj, token, verifier);
        } catch (UserManagerException e) {
            return error(e, "Error while OAuth-like exchange for service: '" + service + "'");
        }
        URL finalRedirectUrl;
        try {
            finalRedirectUrl = um.consumeUserFinalRedirect(userObj.getUsername());
        } catch (UserManagerException e) {
            return error(e, "Error while getting final redirect URL for user '" + username + "' for service '" + service + "'");
        }
        try {
            return Response.temporaryRedirect(finalRedirectUrl.toURI()).build();
        } catch (URISyntaxException e) {
            return error(e, "Malformed redirect URL");
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
            return error(e, "Error while retrieving user '" + username + "'");
        }
        try {
            um.registerService(service, userObj, token);
        } catch (UserManagerException e) {
            return error(e, "Error while OAuth-like exchange for service: '" + service + "'");
        }
        URL finalRedirectUrl;
        try {
            finalRedirectUrl = new URL(
                    "http://" + URLDecoder.decode(redirect, "UTF-8")
            );
        } catch (MalformedURLException e) {
            return error(e, "Error while getting token for user '" + username + "' " +
                            "on service '" + service + "'");
        } catch (UnsupportedEncodingException e) {
            return error(e, "Error while getting token for user '" + username + "' " +
                            "on service '" + service + "'");
        }
        try {
            return Response.temporaryRedirect(finalRedirectUrl.toURI()).build();
        } catch (URISyntaxException e) {
            return error(e, "Malformed redirect URL");
        }
    }

    @DELETE
    @Path("source/{username}/{service}")
    public Response removeSource(
            @PathParam("username") String username,
            @PathParam("service") String service,
            @QueryParam("apikey") String apiKey
    ) {
        try {
            check(
                    this.getClass(),
                    "removeSource",
                    username,
                    service,
                    apiKey
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters");
        }
        UserManager um = instanceManager.getUserManager();
        User userObj;
        try {
            userObj = um.getUser(username);
        } catch (UserManagerException e) {
            return error(e, "Error while retrieving user '" + username + "'");
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
            return error(e, "Error while asking for permissions");
        }
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "You're not allow to do that. Sorry.")
            );
            return rb.build();
        }

        try {
            um.deregisterService(service, userObj);
        } catch (UserManagerException e) {
            return error(e, "Error while retrieving user '" + username + "'");
        }
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "service '" + service + "' removed from user '" + username + "'")
        );
        return rb.build();
    }

    @GET
    @Path("profile/{username}")
    public Response getProfile(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey
    ) {
        try {
            check(
                    this.getClass(),
                    "getProfile",
                    username,
                    apiKey
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters");
        }
        ApplicationsManager am = instanceManager.getApplicationManager();
        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authenticating you application");
        }
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Sorry. You're not allowed to do that.")
            );
            return rb.build();
        }
        ProfileStore ps = instanceManager.getProfileStore();
        UserProfile up;
        try {
            up = ps.getUserProfile(username);
        } catch (ProfileStoreException e) {
            return error(e, "Error while retrieving profile for user '" + username + "'");
        }
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "profile for user '" + username + "' found",
                up
        )
        );
        return rb.build();
    }

    @GET
    @Path("activities/update/{username}")
    public Response forceUserCrawl(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey
    ) {
        try {
            check(
                    this.getClass(),
                    "getUser",
                    username,
                    apiKey
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters");
        }
        UserManager um = instanceManager.getUserManager();
        ApplicationsManager am = instanceManager.getApplicationManager();
        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authenticating your application");
        }
        if (!isAuth) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(new PlatformResponse(
                    PlatformResponse.Status.NOK,
                    "Sorry. You're not allowed to do that.")
            );
            return rb.build();
        }
        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            return error(e, "Error while getting user with username [" + username + "]");
        }
        Crawler crawler = instanceManager.getCrawler();
        Report report;
        try {
            report = crawler.crawl(user.getId());
        } catch (CrawlerException e) {
            return error(e, "Error while getting activities for user [" + username + "]");
        }
        Response.ResponseBuilder rb = Response.ok();
        rb.entity(new PlatformResponse(
                PlatformResponse.Status.OK,
                "activities updated for [" + username + "]",
                report
        )
        );
        return rb.build();
    }

}
