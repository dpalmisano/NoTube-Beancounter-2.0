package tv.notube.profiler.data;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.commons.model.User;
import tv.notube.usermanager.DefaultUserManagerFactory;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerFactoryException;
import tv.notube.usermanager.configuration.ConfigurationManager;
import tv.notube.usermanager.configuration.UserManagerConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Reference test case for {@link DataManagerConfiguration}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DataManagerConfigurationTest {

    private static final String FILEPATH = "user-manager-configuration.xml";

    private DataManagerConfiguration dataManagerConfiguration;

    @BeforeTest
    public void setUp() throws DataManagerConfigurationException {
        UserManagerConfiguration umc = ConfigurationManager
                .getInstance(FILEPATH)
                .getUserManagerConfiguration();
        dataManagerConfiguration = new DataManagerConfiguration(umc);
        dataManagerConfiguration.registerKey(
                "user",
                "test-profiling-line",
                "tv.notube.profiler.data.datasources.UserDataSource"
        );
    }

    @AfterTest
    public void tearDown() {
        dataManagerConfiguration = null;
    }

    @Test
    public void testConfiguration() throws IllegalAccessException,
            InstantiationException, DataSourceException, NoSuchMethodException, InvocationTargetException, UserManagerFactoryException {

        UserManagerConfiguration umc = ConfigurationManager
                .getInstance(FILEPATH)
                .getUserManagerConfiguration();

        UserManager um = DefaultUserManagerFactory
                .getInstance(umc
                ).build();

        Class dataSourceClass = dataManagerConfiguration.getDataSource("user");
        Constructor dsc = dataSourceClass.getConstructor(UserManager.class);
        DataSource dataSource = (DataSource) dsc.newInstance(um);
        dataSource.init();
        RawDataSet rawDataSet = dataSource.getRawData();
        Assert.assertNotNull(rawDataSet);
        Assert.assertEquals(rawDataSet.size(), 1);
        while(rawDataSet.hasNext()) {
            User user = (User) rawDataSet.getNext();
            Assert.assertNotNull(user);
        }
        dataSource.dispose();
    }

}
