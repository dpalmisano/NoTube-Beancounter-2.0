package tv.notube.commons.storage.kvs.mybatis;

import java.util.Properties;

/**
 * It models a generic DAO that could be configured.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class ConfigurableDao {

    protected Properties properties;

    public ConfigurableDao(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }
}
