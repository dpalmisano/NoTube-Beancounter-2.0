package tv.notube.profiler.data.datasources;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.commons.model.User;
import tv.notube.profiler.data.DataSource;
import tv.notube.profiler.data.DataSourceException;
import tv.notube.profiler.data.RawDataSet;
import tv.notube.usermanager.DefaultUserManagerImpl;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.configuration.ConfigurationManager;
import tv.notube.usermanager.configuration.UserManagerConfiguration;

/**
 * Reference test case for {@link UserDataSource}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UserDataSourceTestCase {

    private static final String CONFIG_FILE = "user-manager-configuration.xml";

    private DataSource dataSource;

    @BeforeTest
    public void setUp() throws DataSourceException {
         UserManagerConfiguration configuration
                 = ConfigurationManager
                 .getInstance(CONFIG_FILE)
                 .getUserManagerConfiguration();
        UserManager userManager = new DefaultUserManagerImpl(configuration);
        dataSource = new UserDataSource(userManager);
        dataSource.init();

    }

    @AfterTest
    public void tearDown() throws DataSourceException {
        dataSource.dispose();
        dataSource = null;
    }

    @Test
    public void testGetRawData() throws DataSourceException {
        RawDataSet<User> users = dataSource.getRawData();
        Assert.assertNotNull(users);
        Assert.assertEquals(users.size(), 1);
        User user = users.getNext();
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getName(), "Davide");
    }

}
