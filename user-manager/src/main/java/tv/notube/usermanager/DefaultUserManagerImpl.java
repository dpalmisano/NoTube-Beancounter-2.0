package tv.notube.usermanager;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import tv.notube.commons.model.User;
import tv.notube.commons.model.activity.Activity;
import tv.notube.commons.storage.alog.DefaultActivityLogImpl;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.KVStoreException;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.ActivityLogException;
import tv.notube.commons.storage.model.Query;
import tv.notube.commons.storage.model.fields.*;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;
import tv.notube.commons.storage.model.fields.serialization.SerializationManagerException;
import tv.notube.usermanager.configuration.UserManagerConfiguration;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManager;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManagerFactory;

import java.net.URL;
import java.util.*;

/**
 * {@link UserManager}-based implementation based on {@link KVStore}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultUserManagerImpl extends ConfigurableUserManager {

    private static Logger logger = Logger.getLogger(DefaultUserManagerImpl.class);

    private static final String USERS_TABLE = "users";

    private static final String USER_ACTIVITY_OWNER = COMPONENT + "-%s";

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
        UUID userId = user.getId();
        if(getUser(userId) != null) {
            deleteUser(userId);
            try {
                StringField lastWrite = new StringField(
                        "last_write",
                        Long.toString(System.currentTimeMillis())
                );
                StringField author = new StringField("author", COMPONENT);
                StringField username = new StringField(
                        "username",
                        user.getUsername()
                );
                kvs.setValue(
                        USERS_TABLE,
                        userId.toString(),
                        user,
                        lastWrite,
                        author,
                        username
                );
            } catch (KVStoreException e) {
                final String errMsg = "Error while storing user '" + userId +
                        "' on kvs";
                logger.error(errMsg, e);
                throw new UserManagerException(errMsg, e);
            }
        } else {
            try {
                StringField lastWrite = new StringField(
                        "last_write",
                        Long.toString(System.currentTimeMillis())
                );
                StringField author = new StringField("author", COMPONENT);
                StringField username = new StringField(
                        "username",
                        user.getUsername()
                );
                kvs.setValue(
                        USERS_TABLE,
                        userId.toString(),
                        user,
                        lastWrite,
                        author,
                        username
                );
            } catch (KVStoreException e) {
                final String errMsg = "Error while storing user '" + userId +
                        "' on kvs";
                logger.error(errMsg, e);
                throw new UserManagerException(errMsg, e);
            }
        }
    }

    public User getUser(UUID userId) throws UserManagerException {
        try {
            return (User) kvs.getValue(USERS_TABLE, userId.toString());
        } catch (KVStoreException e) {
            final String errMsg = "Error while retrieving user '" + userId +
                    "' from kvs";
            logger.error(errMsg, e);
            throw new UserManagerException(errMsg, e);
        }
    }

    public User getUser(String username) throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public void storeUserActivities(
            UUID userId,
            List<Activity> activities
    ) throws UserManagerException {
        if (getUser(userId) == null) {
            final String errMsg = "User '" + userId + "' not found";
            logger.error(errMsg);
            throw new UserManagerException(errMsg);
        }
        for (Activity activity : activities) {
            Field[] fields;
            try {
                fields = getActivityLogFields(activity);
            } catch (SerializationManagerException e) {
                final String errMsg = "Error while serializing activity";
                logger.error(errMsg, e);
                throw new UserManagerException(errMsg, e);
            }
            try {
                alog.log(
                        String.format(USER_ACTIVITY_OWNER, userId.toString()),
                        "user activity",
                        fields
                );
            } catch (ActivityLogException e) {
                final String errMsg = "Error while storing activities for " +
                        "user '" + userId + "'";
                logger.error(errMsg, e);
                throw new UserManagerException(errMsg, e);
            }
        }
    }

    public List<Activity> getUserActivities(UUID userId)
            throws UserManagerException {
        if (getUser(userId) == null) {
            final String errMsg = "User '" + userId + "' not found";
            logger.error(errMsg);
            throw new UserManagerException(errMsg);
        }
        tv.notube.commons.storage.model.Activity activities[];
        try {
            activities = alog.filter(
                    new DateTime(),
                    String.format(USER_ACTIVITY_OWNER, userId.toString())
            );
        } catch (ActivityLogException e) {
            final String errMsg = "User while getting activities for '" +
                    userId + "'";
            logger.error(errMsg, e);
            throw new UserManagerException(errMsg, e);
        }
        List<Activity> userActivities = new ArrayList<Activity>();
        SerializationManager sm =
                new SerializationManager();
        for (tv.notube.commons.storage.model.Activity activity : activities) {
            Field fields[];
            try {
                fields = alog.getFields(activity.getId());
            } catch (ActivityLogException e) {
                final String errMsg = "User while getting fields for " +
                        "activity";
                logger.error(errMsg, e);
                throw new UserManagerException(errMsg, e);
            }
            for(Field field : fields) {
                if (field instanceof BytesField) {
                    BytesField bf = (BytesField) field;
                    Activity userActivity;
                    try {
                        userActivity = (Activity) sm.deserialize(
                                bf.getValue().getBytes()
                        );
                    } catch (SerializationManagerException e) {
                        final String errMsg = "User while getting " +
                                "activity field for activity";
                        logger.error(errMsg, e);
                        throw new UserManagerException(errMsg, e);
                    }
                    userActivities.add(userActivity);
                }
            }
        }
        return userActivities;
    }

    public List<Activity> getUserActivities(String username)
            throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public void deleteUser(UUID userId) throws UserManagerException {
        try {
            kvs.deleteValue(USERS_TABLE, userId.toString());
        } catch (KVStoreException e) {
            final String errMsg = "Error while deleting user '" + userId + "' from" +
                    " kvs";
            logger.error(errMsg, e);
            throw new UserManagerException(errMsg, e);
        }
        try {
            alog.delete(String.format(USER_ACTIVITY_OWNER, userId.toString()));
        } catch (ActivityLogException e) {
            final String errMsg = "Error while deleting user '" + userId + "' from" +
                    " alog";
            logger.error(errMsg, e);
            throw new UserManagerException(errMsg, e);
        }
    }

    public List<UUID> getUsersToBeProfiled() throws UserManagerException {
        // TODO (high) replace this with an appropriate method.
        return getUsersToCrawled();
    }

    public List<UUID> getUsersToCrawled() throws UserManagerException {
        List<String> userIds;
        StringField lastWrite = new StringField(
                "last_write",
                Long.toString(System.currentTimeMillis())
        );
        Query query = new Query();
        query.push(lastWrite, Query.Math.LT);
        try {
            userIds = kvs.search(
                    USERS_TABLE,
                    query
            );
        } catch (KVStoreException e) {
            final String errMsg = "Error while getting users to be crawled";
            logger.error(errMsg, e);
            throw new UserManagerException(errMsg, e);
        }
        List<UUID> uuids = new ArrayList<UUID>();
        for (String userId : userIds) {
            uuids.add(UUID.fromString(userId));
        }
        return uuids;
    }

    public void registerService(String service, User user, String token)
            throws UserManagerException {
        throw new UnsupportedOperationException("NYI");
    }

    public ServiceAuthorizationManager getServiceAuthorizationManager()
            throws UserManagerException {
        return sam;
    }

    private Field[] getActivityLogFields(Activity activity)
            throws SerializationManagerException {
        String what = activity.getVerb().name();
        DateTime when = activity.getContext().getDate();
        URL where = activity.getContext().getService();
        StringField whatField = new StringField("verb", what);
        DatetimeField whenField = new DatetimeField("date", when);
        URLField whereField = new URLField("service", where);
        SerializationManager sm =
                new SerializationManager();
        Bytes bytes;
        bytes = sm.serialize(activity);
        BytesField objectField = new BytesField("object", bytes);
        return new Field[]{
                whatField,
                whenField,
                whereField,
                objectField
        };
    }

}
