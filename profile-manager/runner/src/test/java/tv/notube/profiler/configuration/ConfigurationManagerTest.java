package tv.notube.profiler.configuration;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.profiler.container.ProfilingLineContainer;
import tv.notube.profiler.container.ProfilingLineContainerException;
import tv.notube.profiler.data.DataManager;
import tv.notube.profiler.data.DataManagerConfiguration;
import tv.notube.profiler.data.DataManagerException;
import tv.notube.profiler.line.ProfilingLine;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.storage.ProfileStore;
import tv.notube.profiler.storage.ProfileStoreConfiguration;
import tv.notube.profiler.storage.ProfileStoreException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reference test class for {@link ConfigurationManager}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConfigurationManagerTest {

    private final static String CONFIG_FILE = "src/test/resources/configuration.xml";

    private final static String PROFILING_LINE_NAME = "fake-test-profilingline";

    private final static String FIRST_PROFILING_LINE__ITEM_NAME = "first";

    private final static String SECOND_PROFILING_LINE__ITEM_NAME = "second";    

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
    public void testGetProfilingLineContainer() throws ProfilingLineContainerException {
        List<ProfilingLineDescription> pld =
                configurationManager
                        .getConfiguration()
                        .getPlDescriptions();
        Assert.assertNotNull(pld);
        Assert.assertEquals(pld.size(), 1);
        ProfilingLineDescription profilingLineDescription = pld.get(0);
        Assert.assertNotNull(profilingLineDescription);
        Assert.assertEquals(profilingLineDescription.getName(), PROFILING_LINE_NAME);
        ProfilingLineItemDescription[] profilingLineItemDescriptions = profilingLineDescription.getPliDescriptions();
        Assert.assertNotNull(profilingLineItemDescriptions);
        Assert.assertEquals(profilingLineItemDescriptions.length, 2);
        Assert.assertEquals(profilingLineItemDescriptions[0].getName(), FIRST_PROFILING_LINE__ITEM_NAME);
        Assert.assertEquals(profilingLineItemDescriptions[1].getName(), SECOND_PROFILING_LINE__ITEM_NAME);
    }

    @Test
    public void testGetProfileStore() throws ProfileStoreException {
        ProfileStoreConfiguration profileStoreConfiguration =
                configurationManager
                        .getConfiguration()
                        .getProfileStoreConfiguration();
        Assert.assertNotNull(profileStoreConfiguration);
    }

    @Test
    public void testGetDataManager() throws DataManagerException {
        DataManagerConfiguration dataManagerConfiguration =
                configurationManager
                        .getConfiguration()
                        .getDataManagerConfiguration();
        Assert.assertNotNull(dataManagerConfiguration);
        Assert.assertEquals(dataManagerConfiguration.getRegisteredKeys().keySet().size(), 1);
        Assert.assertTrue(dataManagerConfiguration.getRegisteredKeys().containsKey("user"));
        Assert.assertTrue(dataManagerConfiguration.getRegisteredKeys().get("user").contains(PROFILING_LINE_NAME));
    }

}
