package tv.notube.commons.configuration.storage;

import tv.notube.commons.configuration.Configuration;

import java.util.Properties;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class StorageConfiguration extends Configuration {

    private Properties activityLogProperties;

    private Properties kvsProperties;

    public StorageConfiguration(
            Properties activityLogProperties,
            Properties kvsProperties
    ) {
        this.activityLogProperties = activityLogProperties;
        this.kvsProperties = kvsProperties;
    }

    public Properties getActivityLogProperties() {
        return activityLogProperties;
    }

    public Properties getKvsProperties() {
        return kvsProperties;
    }

    @Override
    public String toString() {
        return "StorageConfiguration{" +
                "activityLogProperties=" + activityLogProperties +
                ", kvsProperties=" + kvsProperties +
                "} " + super.toString();
    }
}
