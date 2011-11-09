package tv.notube.usermanager;

import tv.notube.usermanager.configuration.UserManagerConfiguration;

/**
 * Each {@link UserManager} implementation that needs a configuration.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class ConfigurableUserManager implements UserManager {

    protected UserManagerConfiguration configuration;

    public ConfigurableUserManager(UserManagerConfiguration configuration) {
        if(configuration == null)
            throw new IllegalArgumentException("Configuration cannot be null");
        this.configuration = configuration;
    }

    public UserManagerConfiguration getConfiguration() {
        return this.configuration;
    }

}
