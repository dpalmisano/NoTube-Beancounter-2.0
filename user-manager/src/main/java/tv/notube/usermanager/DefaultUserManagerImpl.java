package tv.notube.usermanager;

import org.apache.log4j.Logger;
import tv.notube.commons.alog.*;
import tv.notube.commons.model.User;
import tv.notube.kvs.storage.KVStore;
import tv.notube.kvs.storage.mybatis.MyBatisKVStore;
import tv.notube.kvs.storage.serialization.SerializationManager;
import tv.notube.usermanager.configuration.UserManagerConfiguration;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManager;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManagerFactory;

import java.util.*;

/**
 * {@link UserManager}-based implementation based on {@link KVStore}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultUserManagerImpl extends ConfigurableUserManager {

    private static Logger logger = Logger.getLogger(DefaultUserManagerImpl.class);

    private static final String USERS_TABLE = "users";

    private KVStore kvs;

    private ActivityLog alog;

    private ServiceAuthorizationManager sam;

    public DefaultUserManagerImpl(UserManagerConfiguration configuration) {
        super(configuration);
        Properties prop = configuration.getKvStoreConfiguration().getProperties();
        kvs = new MyBatisKVStore(prop, new SerializationManager());
        // TODO (high) - refactor and make configurable
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:mysql://127.0.0.1:3306/alog");
        properties.setProperty("username", "alog");
        properties.setProperty("password", "alog");
        alog = new DefaultActivityLogImpl(properties);
        sam = ServiceAuthorizationManagerFactory.getInstance().build();
    }

    public void storeUser(User user) throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public User getUser(UUID userId) throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public User getUser(String username) throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public void storeUserActivities(
            UUID userId,
            List<tv.notube.commons.model.activity.Activity> activities
    ) throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public List<tv.notube.commons.model.activity.Activity> getUserActivities(UUID userId)
            throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public List<tv.notube.commons.model.activity.Activity> getUserActivities(String username)
            throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public void deleteUser(UUID userId) throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public List<UUID> getUsersToBeProfiled() throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public List<UUID> getUsersToCrawled() throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public void registerService(String service, User user, String token)
            throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public ServiceAuthorizationManager getServiceAuthorizationManager()
            throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }
}
