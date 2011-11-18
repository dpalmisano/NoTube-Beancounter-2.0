package tv.notube.platform;

import org.apache.log4j.Logger;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;
import tv.notube.profiler.storage.KVProfileStoreImpl;
import tv.notube.profiler.storage.ProfileStore;
import tv.notube.usermanager.DefaultUserManagerFactory;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerFactoryException;
import tv.notube.usermanager.configuration.ConfigurationManager;
import tv.notube.usermanager.configuration.UserManagerConfiguration;

import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LoaderInstanceManager {

    private static LoaderInstanceManager instance =
            new LoaderInstanceManager();

    private UserManager userManager;

    private ProfileStore profileStore;

    private Logger logger = Logger.getLogger(LoaderInstanceManager.class);

    public static LoaderInstanceManager getInstance() {
        if (instance == null)
            instance = new LoaderInstanceManager();

        return instance;
    }

    private LoaderInstanceManager() {
        UserManagerConfiguration userManagerConfiguration =
                ConfigurationManager
                        .getInstance("user-manager-configuration.xml")
                        .getUserManagerConfiguration();
        try {
            userManager = DefaultUserManagerFactory
                    .getInstance(userManagerConfiguration)
                    .build();
        } catch (UserManagerFactoryException e) {
            final String errMsg = "Error while instantiating the User Manager";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
        try {
        Properties properties =
                tv.notube.commons.storage.kvs.configuration.ConfigurationManager
                .getInstance("kvs-configuration.xml")
                .getKVStoreConfiguration()
                .getProperties();
        KVStore kVStore = new MyBatisKVStore(properties, new SerializationManager());
        profileStore = new KVProfileStoreImpl(kVStore);
        } catch (Exception e) {
            final String errMsg = "Error while instantiating the KVStore";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ProfileStore getProfileStore() {
        return profileStore;
    }
}
