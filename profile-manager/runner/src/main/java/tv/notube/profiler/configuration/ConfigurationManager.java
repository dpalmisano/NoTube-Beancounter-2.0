package tv.notube.profiler.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import tv.notube.kvs.storage.configuration.KVStoreConfiguration;
import tv.notube.profiler.data.DataManagerConfiguration;
import tv.notube.profiler.data.DataManagerConfigurationException;
import tv.notube.profiler.data.DataManagerException;
import tv.notube.profiler.line.ProfilingLine;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.storage.ProfileStoreConfiguration;
import tv.notube.usermanager.configuration.UserManagerConfiguration;

import java.io.File;
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
                ProfilingLineItemDescription plid = new ProfilingLineItemDescription(itemName, itemDescription, itemClazz);
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
        UserManagerConfiguration umc = new UserManagerConfiguration(1000, kvsConf);
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

}
