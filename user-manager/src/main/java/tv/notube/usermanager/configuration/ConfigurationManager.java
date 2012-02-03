package tv.notube.usermanager.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import tv.notube.commons.model.Service;
import tv.notube.commons.storage.kvs.configuration.KVStoreConfiguration;
import tv.notube.usermanager.services.auth.AuthHandler;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManagerConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * @author Davide Palmiaano ( dpalmisano@gmail.com )
 */
public class ConfigurationManager {

    private static ConfigurationManager instance;

    private XMLConfiguration xmlConfiguration;

    private KVStoreConfiguration KVStoreConfiguration;

    private ServiceAuthorizationManagerConfiguration samc;

    private UserManagerConfiguration userManagerConfiguration;

    private Properties alogProperties;

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
        initServiceAuthorizationManagerConfiguration();
        initActivityLog();
        userManagerConfiguration = new UserManagerConfiguration(
                pr,
                KVStoreConfiguration,
                samc,
                alogProperties
        );
    }

    private void initActivityLog() {
        HierarchicalConfiguration alog = xmlConfiguration.configurationAt("alog");
        String host = alog.getString("host");
        int port = alog.getInt("port");
        String db = alog.getString("db");
        String username = alog.getString("username");
        String password = alog.getString("password");
        alogProperties = new Properties();
        alogProperties.setProperty("url", "jdbc:mysql://"+ host + ":" + port + "/" + db);
        alogProperties.setProperty("username", username);
        alogProperties.setProperty("password", password);
    }

    private void initServiceAuthorizationManagerConfiguration() {
        List<HierarchicalConfiguration> serviceConfs = xmlConfiguration
                .configurationsAt("services.service");

        samc = new ServiceAuthorizationManagerConfiguration();
        for (HierarchicalConfiguration serviceConf : serviceConfs) {
            String name = serviceConf.getString("[@name]");
            String handler = serviceConf.getString("[@handler]");
            String description = serviceConf.getString("description");
            String apikey = serviceConf.getString("apikey");
            String secret = serviceConf.getString("secret");
            String session = serviceConf.getString("session");
            String endpoint = serviceConf.getString("endpoint");
            Service service = new Service(name);
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

    private long initProfilingRate() {
        return xmlConfiguration.getLong("profiling-rate");
    }

    private void initRecommenderStorageConfiguration() {
        HierarchicalConfiguration pstore = xmlConfiguration.configurationAt("kvs");
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
