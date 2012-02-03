package tv.notube.commons.skos;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import tv.notube.commons.skos.service.SkosConfiguration;

import java.util.Properties;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConfigurationManager {

    private static ConfigurationManager instance;

    private XMLConfiguration xmlConfiguration;

    private SkosConfiguration skosConfiguration;

    public static ConfigurationManager getInstance(String configurationFilePath) {
        if(instance == null) {
            instance = new ConfigurationManager(configurationFilePath);
        }
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
        Properties properties = getProperties();
        skosConfiguration = new SkosConfiguration(properties);
    }

    private Properties getProperties() {
        String host = xmlConfiguration.getString("host");
        String port = xmlConfiguration.getString("port");
        String db = xmlConfiguration.getString("db");
        String username = xmlConfiguration.getString("username");
        String password = xmlConfiguration.getString("password");

        Properties properties = new Properties();
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db;
        properties.setProperty("url", url);
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        return properties;
    }

    public SkosConfiguration getSkosConfiguration() {
        return skosConfiguration;
    }

}
