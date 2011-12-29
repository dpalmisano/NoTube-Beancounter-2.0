package tv.notube.applications;

import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.KVStoreException;
import tv.notube.commons.storage.model.Query;
import tv.notube.commons.storage.model.fields.StringField;

import java.util.List;
import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultApplicationsManagerImpl implements ApplicationsManager {

    private static final String TABLE = "applications";

    private KVStore kvStore;

    public DefaultApplicationsManagerImpl(KVStore kvStore) {
        this.kvStore = kvStore;
    }

    public synchronized String registerApplication(Application application)
            throws ApplicationsManagerException {

        String apiKey;
        if(application.getApiKey() == null) {
            UUID uuidKey = UUID.randomUUID();
            apiKey = uuidKey.toString().replace("-", "");
            application.setApiKey(apiKey);
        } else {
            apiKey = application.getApiKey();
        }
        StringField apiKeyField = new StringField("apiKey", apiKey);
        try {
            kvStore.setValue(TABLE, application.getName(), application, apiKeyField);
        } catch (KVStoreException e) {
            throw new ApplicationsManagerException(
                    "Error while storing application '" + application.getName() + "'",
                    e
            );
        }
        return apiKey;
    }

    public Application getApplication(String name) throws ApplicationsManagerException {
        try {
            return (Application) kvStore.getValue(TABLE, name);
        } catch (KVStoreException e) {
            throw new ApplicationsManagerException(
                    "Error while getting application '" + name + "'" ,
                    e
            );
        }
    }

    public synchronized void grantPermission(String name, Permission permission)
            throws ApplicationsManagerException {
        Application application = getApplication(name);
        if (application == null) {
            throw new ApplicationsManagerException(
                    "Application '" + name + "' not found"
            );
        }
        application.addPermission(permission);
        deregisterApplication(name);
        registerApplication(application);
    }

    public synchronized void grantPermission(
            String name,
            UUID resource,
            Permission.Action action
    ) throws ApplicationsManagerException {
        Application application = getApplication(name);
        if (application == null) {
            throw new ApplicationsManagerException(
                    "Application '" + name + "' not found"
            );
        }
        Permission permission = application.getPermission(resource);
        if (permission == null) {
            permission = new Permission(resource);
            permission.setPermission(action, true);
        } else {
            permission.setPermission(action, true);
        }
        application.addPermission(permission);
        deregisterApplication(name);
        registerApplication(application);
    }

    public synchronized void deregisterApplication(String name) throws
            ApplicationsManagerException {
        try {
            kvStore.deleteValue(TABLE, name);
        } catch (KVStoreException e) {
            throw new ApplicationsManagerException(
                    "Error while deleting application '" + name + "'" ,
                    e
            );
        }
    }

    public boolean isAuthorized(
            String apiKey,
            UUID resource,
            Permission.Action action
    ) throws ApplicationsManagerException {
        Application application;
        application = getApplicationByApiKey(apiKey);
        if(application == null) {
            throw new ApplicationsManagerException(
                    "Application not found. Sorry"
            );
        }
        return application.getPermission(resource).getPermission(action);
    }

    public boolean isAuthorized(String apiKey) throws
            ApplicationsManagerException {
        Application application = getApplicationByApiKey(apiKey);
        if(application == null) {
            return false;
        }
        if(!application.getApiKey().equals(apiKey)) {
            return false;
        }
        return true;
    }

    public Application getApplicationByApiKey(String apiKey)
            throws ApplicationsManagerException {
        Query query = new Query();
        StringField stringField = new StringField("apiKey", apiKey);
        query.push(stringField, Query.Math.EQ);
        List<String> keys;
        try {
            keys = kvStore.search(TABLE, query);
        } catch (KVStoreException e) {
            throw new ApplicationsManagerException(
                    "Error while search for key '" + apiKey + "'",
                    e
            );
        }
        if(keys.size() > 1) {
            throw new ApplicationsManagerException("It seems that there are " +
                    "two applications with the same API key");
        }
        if(keys.size() == 0) {
            return null;
        }
        try {
            return (Application) kvStore.getValue(TABLE, keys.get(0));
        } catch (KVStoreException e) {
            throw new ApplicationsManagerException(
                    "Error while looking up '" + keys.get(0) + "'",
                    e
            );
        }
    }
}
