package tv.notube.platform;

import org.apache.log4j.Logger;
import tv.notube.analytics.Analyzer;
import tv.notube.analytics.DefaultAnalyzerFactoryImpl;
import tv.notube.applications.ApplicationsManager;
import tv.notube.applications.DefaultApplicationsManagerImpl;
import tv.notube.commons.configuration.Configurations;
import tv.notube.commons.configuration.ConfigurationsException;
import tv.notube.commons.configuration.storage.StorageConfiguration;
import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;
import tv.notube.crawler.Crawler;
import tv.notube.crawler.CrawlerFactoryException;
import tv.notube.crawler.DefaultCrawlerFactory;
import tv.notube.profiler.storage.KVProfileStoreImpl;
import tv.notube.profiler.storage.ProfileStore;
import tv.notube.usermanager.DefaultUserManagerFactory;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerFactoryException;

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

    private Crawler crawler;

    public static LoaderInstanceManager getInstance() {
        if (instance == null)
            instance = new LoaderInstanceManager();

        return instance;
    }

    private LoaderInstanceManager() {
        try {
            userManager = DefaultUserManagerFactory.getInstance().build();
        } catch (UserManagerFactoryException e) {
            final String errMsg = "Error while building user manager";
            logger.fatal(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
        KVStore kvs;
        try {
            kvs = getKVS();
        } catch (ConfigurationsException e) {
            final String errMsg = "Error while building kvs";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
        profileStore = new KVProfileStoreImpl(kvs);
        analyzer = DefaultAnalyzerFactoryImpl.getInstance(false).build();
        recommender = new Random(125811727);
        applicationsManager = new DefaultApplicationsManagerImpl(kvs);
        try {
            crawler = DefaultCrawlerFactory.getInstance().build();
        } catch (CrawlerFactoryException e) {
            final String errMsg = "Something went wrong while instantiating the crawler";
            logger.fatal(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }

    private KVStore getKVS() throws ConfigurationsException {
        StorageConfiguration sc;
        sc = Configurations.getConfiguration(
                "storage-configuration.xml",
                StorageConfiguration.class
        );
        return new MyBatisKVStore(sc.getKvsProperties(), new SerializationManager());
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

    public Crawler getCrawler() {
        return crawler;
    }
}
