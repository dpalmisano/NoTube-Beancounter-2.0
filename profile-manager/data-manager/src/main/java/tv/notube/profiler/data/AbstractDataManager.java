package tv.notube.profiler.data;

import tv.notube.commons.configuration.profiler.DataManagerConfiguration;

import java.util.List;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class AbstractDataManager implements DataManager {

    protected DataManagerConfiguration dataManagerConfiguration;

    public AbstractDataManager(DataManagerConfiguration dataManagerConfiguration) {
        this.dataManagerConfiguration = dataManagerConfiguration;
    }

    public DataManagerConfiguration getDataManagerConfiguration() {
        return this.dataManagerConfiguration;
    }

    public Map<String, List<String>> getRegisteredKeys() throws DataManagerException {
        return this.dataManagerConfiguration.getRegisteredKeys();
    }

    public abstract RawDataSet getRawData(String key) throws DataManagerException;

}
