package tv.notube.profiler.data;

import java.util.List;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface DataManager {

    public RawDataSet getRawData(String key) 
            throws DataManagerException;

    public Map<String, List<String>> getRegisteredKeys()
        throws DataManagerException;
    

}
