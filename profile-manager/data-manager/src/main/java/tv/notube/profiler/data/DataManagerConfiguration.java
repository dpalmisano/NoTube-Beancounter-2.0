package tv.notube.profiler.data;


import tv.notube.usermanager.configuration.UserManagerConfiguration;

import java.util.*;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DataManagerConfiguration {

    private Map<String, List<String>> registeredKeys = new HashMap<String, List<String>>();

    private Map<String, Class> registeredDataSources = new HashMap<String, Class>();

    private UserManagerConfiguration userManagerConfiguration;

    public DataManagerConfiguration(UserManagerConfiguration umc) {
        userManagerConfiguration = umc;
    }

    public Map<String, List<String>> getRegisteredKeys() {
        return registeredKeys;
    }

    public void registerKey(String key, String profilingLineName, String dataSourceClassName)
            throws DataManagerConfigurationException {
        if(registeredKeys.containsKey(key)) {
            registeredKeys.get(key).add(profilingLineName);
        } else {
            List<String> profilingLineNames = new ArrayList<String>();
            profilingLineNames.add(profilingLineName);
            registeredKeys.put(key, profilingLineNames);
        }
        try {
            registeredDataSources.put(key, getDataSourceClass(dataSourceClassName));
        } catch (ClassNotFoundException e) {
            throw new DataManagerConfigurationException(
                    "Error while registering '" + dataSourceClassName +
                            "' DataSource for key '" + key + "'", e
            );
        }
    }

    public Class getDataSource(String key) {
        return registeredDataSources.get(key);
    }

    public Map<String, Class> getRegisteredDataSources() {
        return registeredDataSources;
    }

    public List<String> getProfilingLineNames(String key) {
        return registeredKeys.get(key);
    }

    private Class getDataSourceClass(String dataSourceClassName) throws ClassNotFoundException {
        return Class.forName(dataSourceClassName);
    }

    public UserManagerConfiguration getUserManagerConfiguration() {
        return userManagerConfiguration;
    }

}
