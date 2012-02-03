package tv.notube.commons.skos.service;

import java.util.Properties;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SkosConfiguration {

    private Properties properties;

    public SkosConfiguration(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "SkosConfiguration{" +
                "properties=" + properties +
                '}';
    }
}
