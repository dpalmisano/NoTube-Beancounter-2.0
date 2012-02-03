package tv.notube.profiler.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import tv.notube.commons.model.Service;
import tv.notube.commons.storage.kvs.configuration.KVStoreConfiguration;
import tv.notube.profiler.data.DataManagerConfiguration;
import tv.notube.profiler.data.DataManagerConfigurationException;
import tv.notube.profiler.data.DataManagerException;
import tv.notube.profiler.storage.ProfileStoreConfiguration;
import tv.notube.usermanager.configuration.UserManagerConfiguration;
import tv.notube.usermanager.services.auth.AuthHandler;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManagerConfiguration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConfigurationManager {

    private static ConfigurationManager instance;

    public static ConfigurationManager getInstance(String filePath) {
        if(instance == null)
            instance = new ConfigurationManager(filePath);
        return instance;
    }

    private static Logger logger = Logger.getLogger(ConfigurationManager.class);

    private XMLConfiguration xmlConfiguration;

    private ProfilerConfiguration configuration;

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
        } catch (ConfigurationException e) {
            throw new RuntimeException("Error while loading XMLConfiguration", e);
        }
        configuration = new ProfilerConfiguration();
        try {
            initDataManager();
        } catch (DataManagerConfigurationException e) {
            throw new RuntimeException("", e);
        }
        initProfileStore();
        initProfilingLines();

        try {
            checkKeysIntegrity();
        } catch (DataManagerException e) {
            throw new RuntimeException("Error while checking keys integrity", e);
        }

    }

    private void initProfilingLines() {
         List<HierarchicalConfiguration> plines =
                xmlConfiguration.configurationsAt("profiling-lines.profiling-line");
        for(HierarchicalConfiguration pline : plines) {
            String name = pline.getString("name");
            String description = pline.getString("description");
            String clazz = pline.getString("class");
            List<HierarchicalConfiguration> plineItems =
                    pline.configurationsAt("profiling-line-items.profiling-line-item");
            List<ProfilingLineItemDescription> itemsDescriptions
                    = new ArrayList<ProfilingLineItemDescription>();
            for(HierarchicalConfiguration plineItem : plineItems) {
                String itemName = plineItem.getString("name");
                String itemDescription = plineItem.getString("description");
                String itemClazz = plineItem.getString("class");
                List<HierarchicalConfiguration> parameters =
                        plineItem.configurationsAt("parameters");
                Map<String, String> parametersMap = new HashMap<String, String>();
                if(parameters != null) {
                    for (HierarchicalConfiguration parameter : parameters) {
                        String parameterName = parameter.getString("parameter.name");
                        String parameterValue = parameter.getString("parameter.value");
                        parametersMap.put(parameterName, parameterValue);
                    }
                }
                ProfilingLineItemDescription plid = new ProfilingLineItemDescription(
                        itemName,
                        itemDescription,
                        itemClazz,
                        parametersMap
                );
                itemsDescriptions.add(plid);
            }
            ProfilingLineDescription pld = new ProfilingLineDescription(name, description, clazz, itemsDescriptions);
            configuration.add(pld);
        }
    }

    private void initProfileStore() {
        HierarchicalConfiguration pstore = xmlConfiguration.configurationAt("profile-store");
        Map<String, String> nameSpacesConfiguration = new HashMap<String, String>();

        HierarchicalConfiguration tripleStore = pstore.configurationAt("kvs");
        String host = tripleStore.getString("host");
        String port = tripleStore.getString("port");
        String username = tripleStore.getString("username");
        String password = tripleStore.getString("password");

        List<HierarchicalConfiguration> keys = pstore.configurationsAt("tables.table");
        for(HierarchicalConfiguration profileStore : keys) {
            String keyName = profileStore.getString("[@name]");
            String table = profileStore.getString("");
                nameSpacesConfiguration.put(keyName, table);
        }

        ProfileStoreConfiguration profileStoreConfiguration =
                new ProfileStoreConfiguration(host, port, username, password, nameSpacesConfiguration);
        configuration.setProfileStoreConfiguration(profileStoreConfiguration);
    }

    private void initDataManager() throws DataManagerConfigurationException {
        DataManagerConfiguration dataManagerConfiguration;
        HierarchicalConfiguration dmc = xmlConfiguration.configurationAt("data-manager");
        HierarchicalConfiguration kvs = dmc.configurationAt("kvs");
        String host = kvs.getString("host");
        int port = kvs.getInt("port");
        String db = kvs.getString("db");
        String username = kvs.getString("username");
        String password = kvs.getString("password");

        KVStoreConfiguration kvsConf = new KVStoreConfiguration(host, port, db, username, password);
        ServiceAuthorizationManagerConfiguration samc =
                initServiceAuthorizationManagerConfiguration();
        Properties alogProperties = initAlogProperties();
        UserManagerConfiguration umc = new UserManagerConfiguration(
                1000,
                kvsConf,
                samc,
                alogProperties
        );
        dataManagerConfiguration = new DataManagerConfiguration(umc);

        List<HierarchicalConfiguration> keys = dmc.configurationsAt("keys.key");
        for(HierarchicalConfiguration key : keys) {
            String keyString = key.getString("[@name]");
            String dataSourceClassName = key.getString("[@class]");
            List<HierarchicalConfiguration> lines = key.configurationsAt("lines");
            for(HierarchicalConfiguration line : lines) {
                String lineNameString = line.getString("line");
                dataManagerConfiguration.registerKey(
                        keyString,
                        lineNameString,
                        dataSourceClassName
                );
            }
        }
        configuration.setDataManagerConfiguration(dataManagerConfiguration);
    }

    private Properties initAlogProperties() {
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

    private void checkKeysIntegrity() throws DataManagerException {
        ProfileStoreConfiguration profileStoreConfiguration =
                configuration.getProfileStoreConfiguration();
        Map<String, List<String>> registeredKeys
                = configuration.getDataManagerConfiguration().getRegisteredKeys();
        Set<String> psKeys = profileStoreConfiguration.getNameSpacesConfiguration().keySet();
        Set<String> dmKeys =  registeredKeys.keySet();
        if(psKeys.size() != dmKeys.size()) {
            throw new RuntimeException("Error: keys declared for Data Manager are not" +
                    "the same number of the ones declared by the ProfileStore");
        }
        if(!psKeys.equals(dmKeys)) {
            throw new RuntimeException("Error: keys declared for Data Manager are not" +
                    "the same");
        }
    }

    public ProfilerConfiguration getConfiguration() {
        return configuration;
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
