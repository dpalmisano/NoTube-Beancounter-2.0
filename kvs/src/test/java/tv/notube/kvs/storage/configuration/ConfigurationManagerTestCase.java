package tv.notube.kvs.storage.configuration;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.kvs.storage.configuration.ConfigurationManager;
import tv.notube.kvs.storage.configuration.KVStoreConfiguration;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConfigurationManagerTestCase {

    private final static String CONFIG_FILE = "src/test/resources/tv/notube/kv/configuration/kvs-configuration.xml";

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
        KVStoreConfiguration KVStoreConfiguration =
                configurationManager.getKVStoreConfiguration();
        Assert.assertNotNull(KVStoreConfiguration);
    }

}
