package tv.notube.crawler.configuration;

import tv.notube.usermanager.configuration.UserManagerConfiguration;

/**
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
