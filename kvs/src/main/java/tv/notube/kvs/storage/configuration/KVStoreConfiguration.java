package tv.notube.kvs.storage.configuration;

import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class KVStoreConfiguration {

    private String host;

    private int port;

    private String db;

    private String username;

    private String password;

    public KVStoreConfiguration(String host, int port, String db, String username, String password) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.username = username;
        this.password = password;
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("driver", "com.mysql.jdbc.Driver");
        properties.setProperty("url", "jdbc:mysql://" + host + ":" + port + "/" + db + "?allowMultiQueries=true");
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        return properties;
    }
}
