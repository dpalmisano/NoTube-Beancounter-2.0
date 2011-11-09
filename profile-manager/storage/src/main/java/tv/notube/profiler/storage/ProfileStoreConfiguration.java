package tv.notube.profiler.storage;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class wraps a configuration for a
 * {@link ProfileStore}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public final class ProfileStoreConfiguration {

    private String host;

    private String port;

    private String user;

    private String password;

    private Map<String, String> nameSpacesConfiguration = new HashMap<String, String>();


    public ProfileStoreConfiguration(String host, String port, String user, String password,
                                     Map<String, String> nameSpacesConfiguration) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
         if(nameSpacesConfiguration == null || nameSpacesConfiguration.size() == 0) {
            throw new IllegalArgumentException("Namespace configurations, could not be null or missing");
        }
        this.setNameSpacesConfiguration(nameSpacesConfiguration);
    }


    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
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

    public Properties asProperties() {
        Properties properties = new Properties();
        properties.setProperty("host", this.getHost());
        properties.setProperty("port", this.getPort());
        properties.setProperty("user", this.getUser());
        properties.setProperty("password", this.getPassword());
        return properties;
    }

}
