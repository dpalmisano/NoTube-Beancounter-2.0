package tv.notube.usermanager.configuration;

import tv.notube.commons.storage.kvs.configuration.KVStoreConfiguration;

/**
 * Main bean wrapping the configuration properties of
 * the {@link tv.notube.usermanager.ConfigurableUserManager}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UserManagerConfiguration {

    private long profilingRate;

    private KVStoreConfiguration kvStoreConfiguration;

    public UserManagerConfiguration(
            long profilingRate,
            KVStoreConfiguration kvStoreConfiguration
    ) {
        this.profilingRate = profilingRate;
        this.kvStoreConfiguration = kvStoreConfiguration;
    }

    public long getProfilingRate() {
        return profilingRate;
    }

    public KVStoreConfiguration getKvStoreConfiguration() {
        return kvStoreConfiguration;
    }

    @Override
    public String toString() {
        return "UserManagerConfiguration{" +
                "profilingRate=" + profilingRate +
                ", kvStoreConfiguration=" + kvStoreConfiguration +
                '}';
    }
}
