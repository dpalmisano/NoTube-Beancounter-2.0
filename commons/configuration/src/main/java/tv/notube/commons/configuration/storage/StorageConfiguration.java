package tv.notube.commons.configuration.storage;

import tv.notube.commons.configuration.Configuration;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class StorageConfiguration extends Configuration {

    private long profilingRate;

    public StorageConfiguration(
            long profilingRate
    ) {
        this.profilingRate = profilingRate;
    }

    public long getProfilingRate() {
        return profilingRate;
    }

    @Override
    public String toString() {
        return "UserManagerConfiguration{" +
                "profilingRate=" + profilingRate +
                "} " + super.toString();
    }
}
