package tv.notube.profiler.data;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.commons.model.User;
import tv.notube.usermanager.configuration.ConfigurationManager;
import tv.notube.usermanager.configuration.UserManagerConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ModularDataManagerTest {

    private static Logger logger = Logger.getLogger(ModularDataManagerTest.class);

    private static final String FILEPATH = "user-manager-configuration.xml";

    private DataManager dataManager;

    @BeforeTest
    public void setUp() throws DataManagerConfigurationException, DataManagerException {
         UserManagerConfiguration umc = ConfigurationManager
                .getInstance(FILEPATH)
                .getUserManagerConfiguration();
        DataManagerConfiguration dataManagerConfiguration = new DataManagerConfiguration(umc);
        dataManagerConfiguration.registerKey(
                "user",
                "test-profiling-line",
                "tv.notube.profiler.data.datasources.UserDataSource"
        );
        dataManager = new ModularDataManager(dataManagerConfiguration);
    }

    @AfterTest
    public void tearDown() {
        dataManager = null;
    }

    @Test
    public void testGetUserRawData() throws DataManagerException {
        RawDataSet<User> userRawDataSet =
                dataManager.getRawData("user");
        Assert.assertNotNull(userRawDataSet);
        Assert.assertTrue(userRawDataSet.size() >= 0);
        while(userRawDataSet.hasNext()) {
            User user = userRawDataSet.getNext();
            Assert.assertNotNull(user);
            logger.info("Retrieved user: '" + user + "'");
        }
    }

    @Test
    public void testGetProfilingLinesForAKey() throws DataManagerException {
        List<String> userProfilingLines = dataManager.getRegisteredKeys().get("user");
        List<String> expectedUserProfilingLines = new ArrayList<String>();
        expectedUserProfilingLines.add("test-profiling-line");
        Assert.assertTrue(expectedUserProfilingLines.equals(userProfilingLines));
    }
}
