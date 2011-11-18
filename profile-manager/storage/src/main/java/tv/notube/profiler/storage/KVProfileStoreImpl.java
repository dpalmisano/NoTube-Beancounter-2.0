package tv.notube.profiler.storage;

import org.apache.log4j.Logger;
import tv.notube.commons.model.UserProfile;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.KVStoreException;
import tv.notube.commons.storage.model.fields.StringField;

import java.io.OutputStream;
import java.util.UUID;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class KVProfileStoreImpl implements ProfileStore {

    private static final Logger logger = Logger.getLogger(KVProfileStoreImpl.class);

    private final String TABLE = "profiles";

    private KVStore kvs;

    public KVProfileStoreImpl(KVStore kvs) {
        this.kvs = kvs;
    }

    public void storeUserProfile(UserProfile userProfile) throws ProfileStoreException {
        try {
            StringField profiledAt = new StringField("profiled_at", Long.toString(System.currentTimeMillis()));
            kvs.setValue(TABLE, userProfile.getUsername(), userProfile, profiledAt);
        } catch (KVStoreException e) {
            final String errMsg = "Error while storing user profile with id '"
                    + userProfile.getId() + "' for user '" + userProfile.getUsername() + "'";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
    }

    public UserProfile getUserProfile(UUID userId) throws ProfileStoreException {
        throw new UnsupportedOperationException("NIY");
    }

    public UserProfile getUserProfile(String username) throws ProfileStoreException {
        try {
            return (UserProfile) kvs.getValue(TABLE, username);
        } catch (KVStoreException e) {
            final String errMsg = "Error while getting user profile for user  '"
                    + username + "'";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
    }

    public void deleteUserProfile(UUID userId) throws ProfileStoreException {
        throw new UnsupportedOperationException("NIY");
    }

    public void deleteUserProfile(String username) throws ProfileStoreException {
        try {
            kvs.deleteValue(TABLE, username);
        } catch (KVStoreException e) {
            final String errMsg = "Error while deleting user profile for '"
                    + username + "'";
            logger.error(errMsg, e);
            throw new ProfileStoreException(errMsg, e);
        }
    }

    public void export(UUID userId, OutputStream outputStream, Format format)
            throws ProfileStoreException {
        throw new UnsupportedOperationException("NIY");
    }

}
