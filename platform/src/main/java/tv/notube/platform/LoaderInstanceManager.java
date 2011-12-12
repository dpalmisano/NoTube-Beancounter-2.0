package tv.notube.platform;

import org.apache.log4j.Logger;
import tv.notube.analytics.Analyzer;
import tv.notube.analytics.DefaultAnalyzerImpl;
import tv.notube.applications.ApplicationsManager;
import tv.notube.applications.DefaultApplicationsManagerImpl;
import tv.notube.commons.storage.alog.DefaultActivityLogImpl;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;
import tv.notube.profiler.storage.KVProfileStoreImpl;
import tv.notube.profiler.storage.ProfileStore;
import tv.notube.usermanager.DefaultUserManagerFactory;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerFactoryException;
import tv.notube.usermanager.configuration.ConfigurationManager;
import tv.notube.usermanager.configuration.UserManagerConfiguration;

import java.util.Properties;
import java.util.Random;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LoaderInstanceManager {

    private static LoaderInstanceManager instance =
            new LoaderInstanceManager();

    private UserManager userManager;

    private ProfileStore profileStore;

    private Logger logger = Logger.getLogger(LoaderInstanceManager.class);

    private Analyzer analyzer;

    private Random recommender;

    private ApplicationsManager applicationsManager;

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
        Properties properties =
                tv.notube.commons.storage.kvs.configuration.ConfigurationManager
                        .getInstance("kvs-configuration.xml")
                        .getKVStoreConfiguration()
                        .getProperties();
        KVStore kVStore = new MyBatisKVStore(properties, new SerializationManager());
        try {
            profileStore = new KVProfileStoreImpl(kVStore);
        } catch (Exception e) {
            final String errMsg = "Error while instantiating the KVStore";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
        Properties prop = new Properties();
        prop.setProperty("url", "jdbc:mysql://moth.notube.tv:3306/alog");
        prop.setProperty("username", "alog");
        prop.setProperty("password", "alog");
        ActivityLog alog = new DefaultActivityLogImpl(prop);
        analyzer = new DefaultAnalyzerImpl(kVStore, alog);

        recommender = new Random(125811727);

        applicationsManager = new DefaultApplicationsManagerImpl(kVStore);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ProfileStore getProfileStore() {
        return profileStore;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public Random getRecommender() {
        return recommender;
    }

    public ApplicationsManager getApplicationsManager() {
        return applicationsManager;
    }
}
