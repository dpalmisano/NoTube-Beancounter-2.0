package tv.notube.usermanager.configuration;

import tv.notube.commons.storage.kvs.configuration.KVStoreConfiguration;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManagerConfiguration;

import java.util.Properties;

/**
 * Main bean wrapping the configuration properties of
 * the {@link tv.notube.usermanager.ConfigurableUserManager}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UserManagerConfiguration {

    private long profilingRate;

    private KVStoreConfiguration kvStoreConfiguration;

    private ServiceAuthorizationManagerConfiguration samc;

    private Properties properties;

    public UserManagerConfiguration(
            long profilingRate,
            KVStoreConfiguration kvStoreConfiguration,
            ServiceAuthorizationManagerConfiguration samc,
            Properties activityLogProperties
    ) {
        this.profilingRate = profilingRate;
        this.kvStoreConfiguration = kvStoreConfiguration;
        this.samc = samc;
        this.properties = activityLogProperties;
    }

    public long getProfilingRate() {
        return profilingRate;
    }

    public KVStoreConfiguration getKvStoreConfiguration() {
        return kvStoreConfiguration;
    }

    public ServiceAuthorizationManagerConfiguration getServiceAuthorizationManagerConfiguration() {
        return samc;
    }

    public Properties getActivityLogProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "UserManagerConfiguration{" +
                "profilingRate=" + profilingRate +
                ", kvStoreConfiguration=" + kvStoreConfiguration +
                ", samc=" + samc +
                ", properties=" + properties +
                '}';
    }
}
