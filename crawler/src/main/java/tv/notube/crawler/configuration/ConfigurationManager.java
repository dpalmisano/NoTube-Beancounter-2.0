package tv.notube.crawler.configuration;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import tv.notube.commons.model.Service;
import tv.notube.commons.storage.kvs.configuration.KVStoreConfiguration;
import tv.notube.usermanager.configuration.UserManagerConfiguration;
import tv.notube.usermanager.services.auth.AuthHandler;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManagerConfiguration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

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

        ServiceAuthorizationManagerConfiguration samc = initServiceAuthorizationManagerConfiguration();
        Properties alogProperties = getAlogProperties();
        UserManagerConfiguration userManagerConfiguration = new UserManagerConfiguration(
                Long.parseLong(profilingRate),
                kvStoreConfiguration,
                samc,
                alogProperties
        );
        crawlerConfiguration = new CrawlerConfiguration(userManagerConfiguration);
    }

    private Properties getAlogProperties() {
                HierarchicalConfiguration alog = xmlConfiguration.configurationAt("alog");
        String host = alog.getString("host");
        int port = alog.getInt("port");
        String db = alog.getString("db");
        String username = alog.getString("username");
        String password = alog.getString("password");
        Properties alogProperties = new Properties();
        alogProperties.setProperty("url", "jdbc:mysql://"+ host + ":" + port + "/" + db);
        alogProperties.setProperty("username", username);
        alogProperties.setProperty("password", password);
        return alogProperties;
    }

    public CrawlerConfiguration getCrawlerConfiguration() {
        return crawlerConfiguration;
    }

    private ServiceAuthorizationManagerConfiguration initServiceAuthorizationManagerConfiguration() {
        List<HierarchicalConfiguration> serviceConfs = xmlConfiguration
                .configurationsAt("services.service");

        ServiceAuthorizationManagerConfiguration samc = new ServiceAuthorizationManagerConfiguration();
        for(HierarchicalConfiguration serviceConf : serviceConfs) {
            String name = serviceConf.getString("[@name]");
            String handler = serviceConf.getString("[@handler]");
            String description = serviceConf.getString("description");
            String apikey = serviceConf.getString("apikey");
            String secret = serviceConf.getString("secret");
            String session = serviceConf.getString("session");
            String endpoint = serviceConf.getString("endpoint");
            Service service =  new Service(name);
            service.setApikey(apikey);
            service.setDescription(description);
            service.setSecret(secret);
            try {
                service.setEndpoint(new URL(endpoint));
            } catch (MalformedURLException e) {
                final String errMsg = "endpoint for service [" + name + "]" +
                        "is not well-formed";
                throw new RuntimeException(errMsg, e);
            }
            if (session.equals("")) {
                service.setSessionEndpoint(null);
            } else {
                try {
                    service.setSessionEndpoint(new URL(session));
                } catch (MalformedURLException e) {
                    final String errMsg = "session for service [" + name + "]" +
                            "is not well-formed";
                    throw new RuntimeException(errMsg, e);
                }
            }
            Class<? extends AuthHandler> handlerClass = getHandler(handler);
            samc.addService(service, handlerClass);
        }
        return samc;
    }

    private Class<? extends AuthHandler> getHandler(String handler) {
        Class<? extends AuthHandler> handlerClass;
        try {
            handlerClass = (Class<? extends AuthHandler>) Class.forName(handler);
        } catch (ClassNotFoundException e) {
            final String errMsg = "Class [" + handler + "] not found";
            throw new RuntimeException(errMsg, e);
        }
        return handlerClass;
    }

}

