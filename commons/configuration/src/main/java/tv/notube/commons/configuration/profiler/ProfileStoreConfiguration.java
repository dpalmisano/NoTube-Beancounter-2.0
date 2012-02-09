package tv.notube.commons.configuration.profiler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.co,m )
 */
public final class ProfileStoreConfiguration {

    private Map<String, String> nameSpacesConfiguration = new HashMap<String, String>();

    public ProfileStoreConfiguration(Map<String, String> nameSpacesConfiguration) {
         if(nameSpacesConfiguration == null || nameSpacesConfiguration.size() == 0) {
            throw new IllegalArgumentException("Namespace configurations, could not be null or missing");
        }
        this.setNameSpacesConfiguration(nameSpacesConfiguration);
    }

    public Map<String, String> getNameSpacesConfiguration() {
        return nameSpacesConfiguration;
    }

    public void setNameSpacesConfiguration(Map<String, String> nameSpacesConfiguration) {
        if(nameSpacesConfiguration == null || nameSpacesConfiguration.size() == 0) {
            throw new IllegalArgumentException("Namespace configurations, could not be null or missing");
        }
        this.nameSpacesConfiguration = nameSpacesConfiguration;
    }

}
