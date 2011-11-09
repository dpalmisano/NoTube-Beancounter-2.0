package tv.notube.usermanager;

import tv.notube.commons.model.User;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManager;

import java.util.List;
import java.util.UUID;

/**
 * Defines main UserManager methods.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface UserManager {

    public static final String  COMPONENT = "user-manager";

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
     * It handles all the <i>OAuth-like</i> protocols handshaking.
     *
     * @param service
     * @param user
     *@param token  @throws UserManagerException
     */
    public void registerService(String service, User user, String token)
            throws UserManagerException;

    /**
     * Returns the {@link ServiceAuthorizationManager} concrete implementation
     * binding with this user manager.
     *
     * @return
     * @throws UserManagerException
     */
    public ServiceAuthorizationManager getServiceAuthorizationManager()
            throws UserManagerException;

}
