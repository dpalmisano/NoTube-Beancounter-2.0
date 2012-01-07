package tv.notube.platform;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.json.JSONWithPadding;
import tv.notube.applications.Application;
import tv.notube.applications.ApplicationsManager;
import tv.notube.applications.ApplicationsManagerException;
import tv.notube.applications.Permission;
import tv.notube.commons.model.User;
import tv.notube.commons.model.UserProfile;
import tv.notube.commons.model.activity.Activity;
import tv.notube.profiler.storage.ProfileStore;
import tv.notube.profiler.storage.ProfileStoreException;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerException;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Path("/jsonp/user")
@Produces("application/x-javascript")
public class JsonpUserService extends JsonpService {

    @InjectParam
    private InstanceManager instanceManager;

    @POST
    @Path("/register")
    public JSONWithPadding signUp(
            @FormParam("name") String name,
            @FormParam("surname") String surname,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @QueryParam("apikey") String apiKey,
            @QueryParam("callback") String callback
    ) {
        try {
            check(
                    this.getClass(),
                    "signUp",
                    name,
                    surname,
                    username,
                    password,
                    apiKey,
                    callback
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters", callback);
        }

        ApplicationsManager am = instanceManager.getApplicationManager();
        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authorizing your application", callback);
        }
        if (!isAuth) {
            return new JSONWithPadding(new JsonpPlatformResponse(
                    JsonpPlatformResponse.Status.NOK,
                    "Your application is not authorized.Sorry."), callback
            );
        }
        UserManager um = instanceManager.getUserManager();
        try {
            if (um.getUser(username) != null) {
                final String errMsg = "username '" + username + "' is already taken";
                return new JSONWithPadding(
                        new JsonpPlatformResponse(
                                JsonpPlatformResponse.Status.NOK,
                                errMsg
                        ), callback
                );
            }
        } catch (UserManagerException e) {
            final String errMsg = "Error while calling the UserManager";
            return error(e, errMsg, callback);
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
            return error(e, errMsg, callback);
        }

        Application application;
        try {
            application = am.getApplicationByApiKey(apiKey);
        } catch (ApplicationsManagerException e) {
            final String errMsg = "Error while getting application with key '" + apiKey + "'";
            return error(e, errMsg, callback);
        }
        if (application == null) {
            return new JSONWithPadding(
                        new JsonpPlatformResponse(
                                JsonpPlatformResponse.Status.NOK,
                                "Application not found"
                        ), callback
                );
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
            final String errMsg = "Error while granting permissions on user " + user.getId();
            return error(e, errMsg, callback);
        }
        return new JSONWithPadding(new JsonpPlatformResponse(
                JsonpPlatformResponse.Status.OK,
                "user successfully registered",
                user.getId()), callback
        );
    }

    @GET
    @Path("/{username}")
    public JSONWithPadding getUser(
            @PathParam("username")  String username,
            @QueryParam("apikey")   String apiKey,
            @QueryParam("callback") String callback
    ) {
        try {
            check(
                    this.getClass(),
                    "getUser",
                    username,
                    apiKey,
                    callback
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters", callback);
        }

        UserManager um = instanceManager.getUserManager();
        ApplicationsManager am = instanceManager.getApplicationManager();

        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authenticating your application", callback);
        }
        if (!isAuth) {
            return new JSONWithPadding(new JsonpPlatformResponse(
                    JsonpPlatformResponse.Status.NOK,
                    "Sorry. You're not allowed to do that."), callback
            );
        }

        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            final String errMsg = "Error while getting user '" + username + "'.";
            return error(e, errMsg, callback);
        }

        if (user == null) {
            return new JSONWithPadding(new JsonpPlatformResponse(
                    JsonpPlatformResponse.Status.NOK,
                    "user '" + username + "' not found",
                    null), callback
            );
        }
        return new JSONWithPadding(new JsonpPlatformResponse(
                JsonpPlatformResponse.Status.OK,
                "user '" + username + "' found",
                user), callback
        );
    }

    @GET
    @Path("activities/{username}")
    public JSONWithPadding getActivities(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey,
            @QueryParam("callback") String callback
    ) {
        try {
            check(
                    this.getClass(),
                    "getActivities",
                    username,
                    apiKey,
                    callback
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters", callback);
        }
        UserManager um = instanceManager.getUserManager();
        ApplicationsManager am = instanceManager.getApplicationManager();

        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authenticating you application", callback);
        }
        if (!isAuth) {
            return new JSONWithPadding(new JsonpPlatformResponse(
                    JsonpPlatformResponse.Status.NOK,
                    "Sorry. You're not allowed to do that."), callback
            );
        }
        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            return error(e, "Error while retrieving user '" + username + "'", callback);
        }
        if (user == null) {
            Response.ResponseBuilder rb = Response.serverError();
            rb.entity(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "user with username '" + username + "' not found")
            );
            return new JSONWithPadding(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "user with username '" + username + "' not found")
                    ,callback
            );
        }
        List<Activity> activities;
        try {
            activities = um.getUserActivities(user.getId());
        } catch (UserManagerException e) {
            return error(e, "Error while getting user '" + username + "' activities", callback);
        }
        return new JSONWithPadding(
                new JsonpPlatformResponse(
                        JsonpPlatformResponse.Status.OK,
                        "user '" + username + "' activities found",
                        activities)
                , callback
        );
    }

    @DELETE
    @Path("/{username}")
    public JSONWithPadding deleteUser(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey,
            @QueryParam("callback") String callback
    ) {
        try {
            check(
                    this.getClass(),
                    "deleteUser",
                    username,
                    apiKey,
                    callback
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters", callback);
        }
        UserManager um = instanceManager.getUserManager();
        ProfileStore ps = instanceManager.getProfileStore();
        ApplicationsManager am = instanceManager.getApplicationManager();

        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            return error(e, "Error while retrieving user '" + username + "'", callback);
        }
        if (user == null) {
            return new JSONWithPadding(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "user with username '" + username + "' not found")
                    , callback
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
            return error(e, "Error while authorizing your application", callback);

        }
        if (!isAuth) {
            return new JSONWithPadding(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "Sorry, you're not allowed to do that")
                    , callback
            );
        }
        try {
            um.deleteUser(user.getId());
        } catch (UserManagerException e) {
            return error(e, "Error while deleting user '" + username + "'", callback);
        }
        try {
            ps.deleteUserProfile(username);
        } catch (ProfileStoreException e) {
            return error(e, "Error while deleting user '" + username + "'", callback);
        }
        return new JSONWithPadding(
                new JsonpPlatformResponse(
                        JsonpPlatformResponse.Status.OK,
                        "user with username '" + username + "' deleted")
                , callback
        );
    }

    @POST
    @Path("authenticate/{username}")
    public JSONWithPadding authenticate(
            @PathParam("username") String username,
            @FormParam("password") String password,
            @QueryParam("apikey") String apiKey,
            @QueryParam("callback") String callback
    ) {
        try {
            check(
                    this.getClass(),
                    "authenticate",
                    username,
                    password,
                    apiKey,
                    callback
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters", callback);
        }
        UserManager um = instanceManager.getUserManager();
        ApplicationsManager am = instanceManager.getApplicationManager();

        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authenticating your application", callback);
        }
        if (!isAuth) {
            return new JSONWithPadding(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "Sorry, you're not allowed to do that")
                    , callback
            );
        }

        User user;
        try {
            user = um.getUser(username);
        } catch (UserManagerException e) {
            return error(e, "Error while retrieving user '" + username + "'", callback);

        }
        if (user == null) {
            return new JSONWithPadding(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "user with username '" + username + "' not found")
                    , callback
            );
        }
        if (!user.getPassword().equals(password)) {
            return new JSONWithPadding(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "password for '" + username + "' incorrect")
                    , callback
            );
        }
        return new JSONWithPadding(
                new JsonpPlatformResponse(
                        JsonpPlatformResponse.Status.OK,
                        "user '" + username + "' authenticated")
                , callback
        );
    }

    @DELETE
    @Path("source/{username}/{service}")
    public JSONWithPadding removeSource(
            @PathParam("username") String username,
            @PathParam("service") String service,
            @QueryParam("apikey") String apiKey,
            @QueryParam("callback") String callback
    ) {
        try {
            check(
                    this.getClass(),
                    "removeSource",
                    username,
                    service,
                    apiKey,
                    callback
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters", callback);
        }
        UserManager um = instanceManager.getUserManager();
        User userObj;
        try {
            userObj = um.getUser(username);
        } catch (UserManagerException e) {
            return error(e, "Error while retrieving user '" + username + "'", callback);
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
            return error(e, "Error while asking for permissions", callback);
        }
        if (!isAuth) {
            return new JSONWithPadding(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "You're not allow to do that. Sorry.")
                    , callback
            );
        }

        try {
            um.deregisterService(service, userObj);
        } catch (UserManagerException e) {
            return error(e, "Error while retrieving user '" + username + "'", callback);
        }

        return new JSONWithPadding(
                new JsonpPlatformResponse(
                        JsonpPlatformResponse.Status.OK,
                        "service '" + service + "' removed from user '" + username + "'")
                , callback
        );
    }

    @GET
    @Path("profile/{username}")
    public JSONWithPadding getProfile(
            @PathParam("username") String username,
            @QueryParam("apikey") String apiKey,
            @QueryParam("callback") String callback
    ) {
        try {
            check(
                    this.getClass(),
                    "getProfile",
                    username,
                    apiKey,
                    callback
            );
        } catch (ServiceException e) {
            return error(e, "Error while checking parameters", callback);
        }
        ApplicationsManager am = instanceManager.getApplicationManager();
        boolean isAuth;
        try {
            isAuth = am.isAuthorized(apiKey);
        } catch (ApplicationsManagerException e) {
            return error(e, "Error while authenticating you application", callback);
        }
        if (!isAuth) {
            return new JSONWithPadding(
                    new JsonpPlatformResponse(
                            JsonpPlatformResponse.Status.NOK,
                            "Sorry. You're not allowed to do that.")
                    , callback
            );
        }
        ProfileStore ps = instanceManager.getProfileStore();
        UserProfile up;
        try {
            up = ps.getUserProfile(username);
        } catch (ProfileStoreException e) {
            return error(e, "Error while retrieving profile for user '" + username + "'", callback);
        }
        return new JSONWithPadding(
                new JsonpPlatformResponse(
                        JsonpPlatformResponse.Status.OK,
                        "profile for user '" + username + "' found",
                        up)
                , callback
        );
    }

}