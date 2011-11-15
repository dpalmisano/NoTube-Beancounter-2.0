package tv.notube.usermanager;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.commons.model.Auth;
import tv.notube.commons.model.Service;
import tv.notube.commons.model.User;
import tv.notube.usermanager.configuration.ConfigurationManager;
import tv.notube.usermanager.configuration.UserManagerConfiguration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

/**
 * Reference test case for {@link UserManager}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UserManagerTestCase {

    private final static String CONFIG_FILE = "tv/notube/usermanager/configuration/user-manager-configuration.xml";

    private UserManager userManager;

    @BeforeTest
    public void setUp() {
         UserManagerConfiguration configuration
                 = ConfigurationManager
                 .getInstance(CONFIG_FILE)
                 .getUserManagerConfiguration();
        userManager = new DefaultUserManagerImpl(configuration);
    }

    @AfterTest
    public void tearDown() {
        userManager = null;
    }

    @Test
    public void testCRUD() throws URISyntaxException, UserManagerException {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setName("Libby");
        user.setSurname("Miller");
        user.setForcedProfiling(false);
        user.setReference(new URI("http://notube.tv/user/" + userId));
        user.setProfiledAt(new DateTime());

        Service service = new Service();
        service.setName("facebook");
        user.addService(service.getName(), new Auth("", null));

        userManager.storeUser(user);
        User actual = userManager.getUser(userId);
        Assert.assertEquals(user, actual);

        List<UUID> users = userManager.getUsersToCrawled();
        Assert.assertTrue(users.contains(userId));

        userManager.deleteUser(userId);
        actual = userManager.getUser(userId);
        Assert.assertNull(actual);
    }

}
