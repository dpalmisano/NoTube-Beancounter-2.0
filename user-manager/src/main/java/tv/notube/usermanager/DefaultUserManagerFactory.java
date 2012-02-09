package tv.notube.usermanager;

import tv.notube.commons.configuration.Configurations;
import tv.notube.commons.configuration.ConfigurationsException;
import tv.notube.commons.configuration.auth.ServiceAuthorizationManagerConfiguration;
import tv.notube.commons.configuration.storage.StorageConfiguration;
import tv.notube.commons.configuration.usermanager.UserManagerConfiguration;
import tv.notube.commons.storage.alog.DefaultActivityLogImpl;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManager;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManagerFactory;

import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultUserManagerFactory implements UserManagerFactory {

    private static final String USERMANAGER_CONF = "usermanager-configuration.xml";

    private static final String STORAGE_CONF = "storage-configuration.xml";

    private static final String SAM_CONF = "sam-configuration.xml";

    private static UserManagerFactory instance;

    public static synchronized UserManagerFactory getInstance() {
        if(instance == null) {
            instance = new DefaultUserManagerFactory();
        }
        return instance;
    }

    private DefaultUserManagerFactory() {
        UserManagerConfiguration umc;
        try {
            umc = Configurations.getConfiguration(
                    USERMANAGER_CONF,
                    UserManagerConfiguration.class
            );
        } catch (ConfigurationsException e) {
            final String errMsg = "Error while loading configuration from" +
                    "[" + USERMANAGER_CONF + "]";
            throw new RuntimeException(errMsg, e);
        }
        StorageConfiguration sc;
        try {
            sc = Configurations.getConfiguration(
                    STORAGE_CONF,
                    StorageConfiguration.class
            );
        } catch (ConfigurationsException e) {
            final String errMsg = "Error while loading configuration from" +
                    "[" + STORAGE_CONF + "]";
            throw new RuntimeException(errMsg, e);
        }
        ServiceAuthorizationManagerConfiguration samc;
        try {
            samc = Configurations.getConfiguration(
                    SAM_CONF,
                    ServiceAuthorizationManagerConfiguration.class
            );
        } catch (ConfigurationsException e) {
            final String errMsg = "Error while loading configuration from" +
                    "[" + STORAGE_CONF + "]";
            throw new RuntimeException(errMsg, e);
        }
        ServiceAuthorizationManager sam = getSam(samc);
        KVStore kvs = getKVS(sc.getKvsProperties());
        ActivityLog alog = getAlog(sc.getActivityLogProperties());
        userManager = new DefaultUserManagerImpl(kvs, alog, sam, umc.getProfilingRate());
    }

    private ActivityLog getAlog(Properties activityLogProperties) {
        return new DefaultActivityLogImpl(activityLogProperties);
    }

    private KVStore getKVS(Properties kvsProperties) {
        return new MyBatisKVStore(kvsProperties, new SerializationManager());
    }

    private ServiceAuthorizationManager getSam(ServiceAuthorizationManagerConfiguration samc) {
        return ServiceAuthorizationManagerFactory.getInstance(samc).build();
    }

    private UserManager userManager;

    public UserManager build() throws UserManagerFactoryException {
        return userManager;
    }
}
