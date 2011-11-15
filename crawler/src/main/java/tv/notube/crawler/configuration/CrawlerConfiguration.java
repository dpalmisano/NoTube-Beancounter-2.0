package tv.notube.crawler.configuration;

import tv.notube.usermanager.configuration.UserManagerConfiguration;

/**
 * This class models the configuration for the {@link tv.notube.crawler.Crawler}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class CrawlerConfiguration {

    private UserManagerConfiguration userManagerConfiguration;

    public CrawlerConfiguration(UserManagerConfiguration userManagerConfiguration) {
        this.userManagerConfiguration = userManagerConfiguration;
    }

    public UserManagerConfiguration getUserManagerConfiguration() {
        return userManagerConfiguration;
    }
}
