package tv.notube.commons.configuration.usermanager;

import tv.notube.commons.configuration.Configuration;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UserManagerConfiguration extends Configuration {

    private long profilingRate;

    public UserManagerConfiguration(
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
