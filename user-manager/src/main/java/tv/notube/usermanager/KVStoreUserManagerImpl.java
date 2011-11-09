package tv.notube.usermanager;

import org.apache.log4j.Logger;
import tv.notube.commons.model.Service;
import tv.notube.commons.model.User;
import tv.notube.kvs.storage.Field;
import tv.notube.kvs.storage.KVStore;
import tv.notube.kvs.storage.KVStoreException;
import tv.notube.kvs.storage.mybatis.MyBatisKVStore;
import tv.notube.kvs.storage.serialization.SerializationManager;
import tv.notube.usermanager.configuration.UserManagerConfiguration;
import tv.notube.usermanager.fields.Author;
import tv.notube.usermanager.fields.LastWrite;
import tv.notube.usermanager.fields.Username;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManager;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManagerException;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManagerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class KVStoreUserManagerImpl extends ConfigurableUserManager {

    private static Logger logger = Logger.getLogger(KVStoreUserManagerImpl.class);

    private static final String USERS_TABLE = "users";

    private KVStore kvs;

    private ServiceAuthorizationManager sam;

    public KVStoreUserManagerImpl(UserManagerConfiguration configuration) {
        super(configuration);
        Properties prop = configuration.getKvStoreConfiguration().getProperties();
        kvs = new MyBatisKVStore(prop, new SerializationManager());
        sam = ServiceAuthorizationManagerFactory.getInstance().build();
    }

    public void storeUser(User user) throws UserManagerException {
        UUID userId = user.getId();
        String userIdStr = userId.toString();
        try {
            if (kvs.getValue(USERS_TABLE, userIdStr) == null) {
                kvs.setValue(
                        USERS_TABLE,
                        userIdStr,
                        user,
                        new LastWrite(),
                        new Author(COMPONENT),
                        new Username(user.getUsername())
                );
            } else {
                kvs.deleteValue(USERS_TABLE, userIdStr);
                kvs.setValue(
                        USERS_TABLE,
                        userIdStr,
                        user,
                        new LastWrite(),
                        new Author(COMPONENT),
                        new Username(user.getUsername())
                );
            }
        } catch (KVStoreException e) {
            final String errMsg = "Error while storing data of user: '" + userId + "'";
            logger.error(errMsg, e);
            throw new UserManagerException(errMsg, e);
        }
    }

    public User getUser(String userName) throws UserManagerException {
        Username q = new Username(userName);
        List<String> userIds;
        try {
            userIds = kvs.search(USERS_TABLE, KVStore.Boolean.AND, q);
        } catch (KVStoreException e) {
            throw new UserManagerException("", e);
        }
        if(userIds.size() != 1) {
            return null;
        }
        try {
            return (User) kvs.getValue(USERS_TABLE, userIds.get(0));
        } catch (KVStoreException e) {
            throw new UserManagerException("", e);
        }
    }

    public User getUser(UUID userId) throws UserManagerException {
        try {
            return (User) kvs.getValue(USERS_TABLE, userId.toString());
        } catch (KVStoreException e) {
            throw new UserManagerException("", e);
        }
    }

    public void deleteUser(UUID userId) throws UserManagerException {
        try {
            kvs.deleteValue(USERS_TABLE, userId.toString());
        } catch (KVStoreException e) {
            throw new UserManagerException("", e);
        }
    }

    public List<UUID> getUsersToCrawled() throws UserManagerException {
        long rate = System.currentTimeMillis() - configuration.getProfilingRate();
        Field f = new Field();
        f.setName(LastWrite.NAME);
        f.setValue(Long.toString(rate));
        List<UUID> result = new ArrayList<UUID>();
        try {
            for (String id : kvs.search(USERS_TABLE, KVStore.Math.LESS, f)) {
                result.add(UUID.fromString(id));
            }
        } catch (KVStoreException e) {
            throw new UserManagerException("", e);
        }
        return result;
    }

    public List<UUID> getUsersToBeProfiled() throws UserManagerException {
        // TODO (high) for now it's the same of ones to be crawled
        return getUsersToCrawled();
    }

    public void registerService(String service, User user, String token)
            throws UserManagerException {
        User authUser;
        try {
            authUser = sam.register(user, service, token);
        } catch (ServiceAuthorizationManagerException e) {
            throw new UserManagerException("Error while granting OAuth grant", e);
        }
        storeUser(authUser);
    }

    public ServiceAuthorizationManager getServiceAuthorizationManager()
            throws UserManagerException {
        return this.sam;
    }

}
