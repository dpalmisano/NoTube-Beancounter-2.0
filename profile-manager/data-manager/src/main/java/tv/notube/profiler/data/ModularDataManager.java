package tv.notube.profiler.data;

import tv.notube.commons.configuration.profiler.DataManagerConfiguration;
import tv.notube.usermanager.UserManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ModularDataManager extends AbstractDataManager {

    private Map<String, DataSource> datasources = new HashMap<String, DataSource>();

    private UserManager userManager;

    public ModularDataManager(
            UserManager userManager,
            DataManagerConfiguration dataManagerConfiguration
    ) throws DataManagerException {
        super(dataManagerConfiguration);
        this.userManager = userManager;
        for (String key : dataManagerConfiguration.getRegisteredDataSources().keySet()) {
            Class dataSourceClassName = dataManagerConfiguration.getDataSource(key);
            DataSource dataSource;
            Constructor dsc;
            try {
                dsc = dataSourceClassName.getConstructor(UserManager.class);
            } catch (NoSuchMethodException e) {
                throw new DataManagerException("Error while looking up the proper constructor", e);
            }
            try {
                dataSource = (DataSource) dsc.newInstance(userManager);
            } catch (InstantiationException e) {
                throw new DataManagerException("Error while instantiating the proper DataSource", e);
            } catch (IllegalAccessException e) {
                throw new DataManagerException("Error while accessing the constructor of DataSource", e);
            } catch (InvocationTargetException e) {
                throw new DataManagerException("Error while constructing the proper DataSource", e);
            }
            try {
                dataSource.init();
            } catch (DataSourceException e) {
                throw new DataManagerException(
                        "Error while initializing data source associated with key:'" + key + "'",
                        e
                );
            }
            datasources.put(key, dataSource);
        }
    }

    @Override
    public RawDataSet getRawData(String key) throws DataManagerException {
        try {
            return datasources.get(key).getRawData();
        } catch (DataSourceException e) {
            throw new DataManagerException("Error while getting raw data for key: '" + key + "'");
        }
    }
}
