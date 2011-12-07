package tv.notube.usermanager;

import tv.notube.commons.model.User;
import tv.notube.commons.model.activity.Activity;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManager;
import tv.notube.usermanager.services.auth.oauth.OAuthToken;

import java.util.List;
import java.util.UUID;

/**
 * Defines main UserManager methods.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface UserManager {

    public static final String COMPONENT = "user-manager";

    /**
     * Store a {@link User} on the Beancounter.
     *
     * @param
     * @throws UserManagerException
     */
    public void storeUser(User user) throws UserManagerException;

    /**
     * Retrieve a {@link User} from the Beancounter.
     *
     * @param userId
     * @return
     * @throws UserManagerException
     */
    public User getUser(UUID userId) throws UserManagerException;

    /**
     * Retrieve a {@link User} from the Beancounter.
     *
     * @param username
     * @return
     * @throws UserManagerException
     */
    public User getUser(String username) throws UserManagerException;

    /**
     * Store a list of {@link Activity} for a specific user.
     *
     * @param userId
     * @param activities
     * @throws UserManagerException
     */
    public void storeUserActivities(UUID userId, List<Activity> activities)
            throws UserManagerException;

    /**
     * Retrieve all the stored {@link Activity} of a {@link User} using
     * its <i>identifier</i>.
     *
     * @param userId
     * @return
     * @throws UserManagerException
     */
    public List<Activity> getUserActivities(UUID userId)
            throws UserManagerException;

    /**
     * Retrieve all the stored {@link Activity} of a {@link User} using
     * its <i>username</i>.
     *
     * @param username
     * @return
     * @throws UserManagerException
     */
    public List<Activity> getUserActivities(String username)
            throws UserManagerException;

    /**
     * Completely flushes out all the {@link User} data.
     *
     * @param userId
     * @throws UserManagerException
     */
    public void deleteUser(UUID userId) throws UserManagerException;

    /**
     * Returns a list of {@link User} identifiers to be profiled.
     *
     * @return
     * @throws UserManagerException
     */
    public List<UUID> getUsersToBeProfiled() throws UserManagerException;

    /**
     * Return a list of {@link User} identifiers to be crawled.
     *
     * @return
     * @throws UserManagerException
     */
    public List<UUID> getUsersToCrawled() throws UserManagerException;

    /**
     * Get the user <a href="http://oauth.net">OAuth</> token.
     *
     * @param service
     * @param username
     * @return
     */
    OAuthToken getOAuthToken(String service, String username)
            throws UserManagerException;

    /**
     * It handles all the <i>OAuth-like</i> protocols handshaking.
     *
     * @param service
     * @param user
     * @param token   @throws UserManagerException
     */
    public void registerService(
            String service,
            User user,
            String token
    ) throws UserManagerException;

    /**
     * It handles all the <i>OAuth</i> protocol handshaking.
     *
     * @param service
     * @param user
     * @param token   @throws UserManagerException
     */
    public void registerOAuthService(
            String service,
            User user,
            String token,
            String verifier
    ) throws UserManagerException;

    /**
     * Returns the {@link ServiceAuthorizationManager} concrete implementation
     * binding with this user manager.
     *
     * @return
     * @throws UserManagerException
     */
    public ServiceAuthorizationManager getServiceAuthorizationManager()
            throws UserManagerException;

    /**
     * Removes from the provided {@link User} a service with the name
     * provided as input.
     *
     * @param service
     * @param userObj
     */
    void deregisterService(String service, User userObj) throws UserManagerException;
}
