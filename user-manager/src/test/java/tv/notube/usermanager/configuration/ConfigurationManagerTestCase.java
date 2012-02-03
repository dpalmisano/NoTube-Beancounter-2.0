package tv.notube.usermanager.configuration;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConfigurationManagerTestCase {

    private final static String CONFIG_FILE = "tv/notube/usermanager/configuration/user-manager-configuration.xml";

    private ConfigurationManager configurationManager;

    @BeforeTest
    public void setUp() {
        configurationManager = ConfigurationManager.getInstance(CONFIG_FILE);
    }

    @AfterTest
    public void tearDown() {
        configurationManager = null;
    }

    @Test
    public void testGetConfigurations() {
        UserManagerConfiguration userManagerConfiguration =
                configurationManager.getUserManagerConfiguration();
        Assert.assertNotNull(userManagerConfiguration);
        Assert.assertEquals(
                userManagerConfiguration
                        .getServiceAuthorizationManagerConfiguration()
                        .getServices()
                        .size(),
                3
        );
    }

}
