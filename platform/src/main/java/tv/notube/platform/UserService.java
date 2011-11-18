package tv.notube.platform;

import com.sun.jersey.api.core.InjectParam;
import org.apache.http.protocol.ResponseServer;
import tv.notube.commons.model.Service;
import tv.notube.commons.model.User;
import tv.notube.commons.model.UserProfile;
import tv.notube.profiler.storage.ProfileStore;
import tv.notube.profiler.storage.ProfileStoreException;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
            @FormParam("password") String password
    ) {
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
        return new Response(
                Response.Status.OK,
                "user successfully registered",
                user.getId()
        );
    }

    @GET
    @Path("/{username}")
    public Response getUser(@PathParam("username") String username) {
        UserManager um = instanceManager.getUserManager();
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

    public void signIn() {}

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

    @GET
    @Path("/profile/{username}")
    public Response getProfile( @PathParam("username") String username ) {
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
