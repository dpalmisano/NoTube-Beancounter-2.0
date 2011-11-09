package tv.notube.usermanager.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import tv.notube.kvs.storage.configuration.KVStoreConfiguration;

import java.io.File;

/**
 * @author Davide Palmiaano ( dpalmisano@gmail.com )
 */
public class ConfigurationManager {

    private static ConfigurationManager instance;

    private XMLConfiguration xmlConfiguration;

    private KVStoreConfiguration KVStoreConfiguration;

    private UserManagerConfiguration userManagerConfiguration;

    private static Logger logger = Logger.getLogger(ConfigurationManager.class);

    public static ConfigurationManager getInstance(String filePath) {
        if (instance == null)
            instance = new ConfigurationManager(filePath);
        return instance;
    }

    private ConfigurationManager(String configurationFilePath) {
        if (configurationFilePath == null)
            throw new IllegalArgumentException("Configuration file path cannot be null");

        try {
            xmlConfiguration = new XMLConfiguration(
                    this.getClass()
                            .getClassLoader()
                            .getResource(configurationFilePath)
            );
        } catch (ConfigurationException e) {
            throw new RuntimeException("Error while loading XMLConfiguration", e);
        }
        long pr = initProfilingRate();
        initRecommenderStorageConfiguration();
        userManagerConfiguration = new UserManagerConfiguration(pr, KVStoreConfiguration);
    }

    private long initProfilingRate() {
        return xmlConfiguration.getLong("profiling-rate");
    }

    private void initRecommenderStorageConfiguration() {
        HierarchicalConfiguration pstore = xmlConfiguration.configurationAt("relational");
        String host = pstore.getString("host");
        int port = pstore.getInt("port");
        String db = pstore.getString("db");
        String username = pstore.getString("username");
        String password = pstore.getString("password");
        KVStoreConfiguration =
                new KVStoreConfiguration(host, port, db, username, password);
    }

    public UserManagerConfiguration getUserManagerConfiguration() {
        return userManagerConfiguration;
    }

}
