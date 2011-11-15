package tv.notube.crawler.configuration;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import tv.notube.kvs.storage.configuration.KVStoreConfiguration;
import tv.notube.usermanager.configuration.UserManagerConfiguration;

import java.io.File;

/**
 * This class is responsible to parse the configuration file
 * and instantiate a valid not <code>null</code>
 * {@link tv.notube.crawler.configuration.CrawlerConfiguration}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConfigurationManager {

    private static ConfigurationManager instance;

    public static ConfigurationManager getInstance(String filePath) {
        if(instance == null)
            instance = new ConfigurationManager(filePath);
        return instance;
    }

    private XMLConfiguration xmlConfiguration;

    private CrawlerConfiguration crawlerConfiguration;

    private static Logger logger = Logger.getLogger(ConfigurationManager.class);

    private ConfigurationManager(String configurationFilePath) {
        if(configurationFilePath == null)
            throw new IllegalArgumentException("Configuration file path cannot be null");

        File configurationFile = new File(configurationFilePath);
        if(!configurationFile.exists()) {
            logger.error("Configuration file: '" +
                    configurationFilePath + "' does not exists");
            throw new IllegalArgumentException("Configuration file: '" +
                    configurationFilePath + "' does not exists");
        }
        try {
            xmlConfiguration = new XMLConfiguration(configurationFile.getAbsolutePath());
        } catch (org.apache.commons.configuration.ConfigurationException e) {
            throw new RuntimeException("Error while loading XMLConfiguration", e);
        }
        initUserManager();
    }

    private void initUserManager() {
        HierarchicalConfiguration kvs = xmlConfiguration.configurationAt("kvs");
        String profilingRate = kvs.getString("profiling-rate");
        HierarchicalConfiguration pstore = kvs.configurationAt("relational");
        String host = pstore.getString("host");
        int port = pstore.getInt("port");
        String db = pstore.getString("db");
        String username = pstore.getString("username");
        String password = pstore.getString("password");
        KVStoreConfiguration kvStoreConfiguration =
                new KVStoreConfiguration(host, port, db, username, password);

        UserManagerConfiguration userManagerConfiguration =
                new UserManagerConfiguration(Long.parseLong(profilingRate), kvStoreConfiguration);
        crawlerConfiguration = new CrawlerConfiguration(userManagerConfiguration);
    }

    public CrawlerConfiguration getCrawlerConfiguration() {
        return crawlerConfiguration;
    }

}

