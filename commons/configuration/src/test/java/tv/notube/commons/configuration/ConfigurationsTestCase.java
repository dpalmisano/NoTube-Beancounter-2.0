package tv.notube.commons.configuration;

import org.testng.Assert;
import org.testng.annotations.Test;
import tv.notube.commons.configuration.analytics.AnalyticsConfiguration;
import tv.notube.commons.configuration.auth.ServiceAuthorizationManagerConfiguration;
import tv.notube.commons.configuration.profiler.ProfilerConfiguration;
import tv.notube.commons.configuration.storage.StorageConfiguration;
import tv.notube.commons.configuration.usermanager.UserManagerConfiguration;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConfigurationsTestCase {

    @Test
    public void testUserManagerConfiguration() throws ConfigurationsException {
        UserManagerConfiguration userManagerConfiguration = Configurations.getConfiguration(
                "usermanager-configuration.xml",
                UserManagerConfiguration.class
        );
        Assert.assertNotNull(userManagerConfiguration);
        Assert.assertEquals(userManagerConfiguration.getProfilingRate(), 10);
    }

    @Test
    public void testAnalyticsConfiguration() throws ConfigurationsException {
        AnalyticsConfiguration analyticsConfiguration = Configurations.getConfiguration(
                "analytics-configuration.xml",
                AnalyticsConfiguration.class
        );
        Assert.assertNotNull(analyticsConfiguration);
    }

    @Test
    public void testProfilerConfiguration() throws ConfigurationsException {
        ProfilerConfiguration profilerConfiguration = Configurations.getConfiguration(
                "profiler-configuration.xml",
                ProfilerConfiguration.class
        );
        Assert.assertNotNull(profilerConfiguration);
    }

    @Test
    public void testServiceAuthorizationManagerConfiguration() throws ConfigurationsException {
        ServiceAuthorizationManagerConfiguration samConfiguration = Configurations.getConfiguration(
                "sam-configuration.xml",
                ServiceAuthorizationManagerConfiguration.class
        );
        Assert.assertNotNull(samConfiguration);
        Assert.assertEquals(samConfiguration.getServices().size(), 3);
    }

    @Test
    public void testStorageConfiguration() throws ConfigurationsException {
        StorageConfiguration sConfiguration = Configurations.getConfiguration(
                "storage-configuration.xml",
                StorageConfiguration.class
        );
        Assert.assertNotNull(sConfiguration);
    }

}
