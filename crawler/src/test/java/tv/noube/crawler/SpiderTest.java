package tv.noube.crawler;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.commons.model.OAuthAuth;
import tv.notube.commons.model.SimpleAuth;
import tv.notube.commons.model.Service;
import tv.notube.commons.model.User;
import tv.notube.commons.model.activity.Activity;
import tv.notube.crawler.runnable.Spider;
import tv.notube.crawler.runnable.SpiderException;
import tv.notube.usermanager.DefaultUserManagerFactory;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerException;
import tv.notube.usermanager.UserManagerFactoryException;
import tv.notube.usermanager.configuration.ConfigurationManager;
import tv.notube.usermanager.configuration.UserManagerConfiguration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SpiderTest {

    private static final String CONFIG_FILE = "tv/notube/crawler/configuration/usermanager-configuration.xml";

    private UserManager um;

    private static final String USERID = "9550da4c-42c2-4ad7-9a94-962b8e4ac3e3";

    private Spider spider;

    @BeforeTest
    public void setUp()
            throws UserManagerFactoryException, UserManagerException,
            URISyntaxException, SpiderException {
        UserManagerConfiguration umc = ConfigurationManager
                .getInstance(CONFIG_FILE)
                .getUserManagerConfiguration();
        um = DefaultUserManagerFactory.getInstance(umc).build();
        if (!initiCheck(um, USERID)) {
            initUserManager(um);
        }
        spider = new Spider(
                "test-spider",
                um,
                UUID.fromString("9550da4c-42c2-4ad7-9a94-962b8e4ac3e3")
        );
    }


    private boolean initiCheck(UserManager um, String userid) throws UserManagerException {
        User user = um.getUser(UUID.fromString(userid));
        if (user == null) {
            return false;
        }
        return true;
    }

    private void initUserManager(UserManager um)
            throws URISyntaxException, UserManagerException {
        User user = new User();
        UUID userId = UUID.fromString(USERID);
        user.setId(userId);
        user.setUsername("test");
        user.setName("test");
        user.setSurname("user");
        user.setForcedProfiling(false);
        user.setReference(new URI("http://notube.tv/user/test"));
        user.setProfiledAt(new DateTime());

        Service lastfm = new Service();
        lastfm.setName("lastfm");
        user.addService(lastfm.getName(), new SimpleAuth("af65659c785315b90b54eea682e66433", "davidepalmisano"));

        Service twitter = new Service();
        twitter.setName("twitter");
        user.addService(twitter.getName(), new OAuthAuth(
                "14656799-yAIIzOoFT8ILah3AaDLX4eG3DEPUNhP9d8tqafys",
                "em5rrY6IzNwmauUlecsf7zbdaUXZaDdLj4jNRq6TI50")
        );
        Service facebook = new Service();
        facebook.setName("facebook");
        user.addService(facebook.getName(), new OAuthAuth(
                "AAAEdCZCT7jlwBACmL69H9HgqDJlZC3tJTBzSxTCnaVKAE1uTwextb0lgSfZAY8ybgWPjMnSFo37QhjU7SOImT0bRxTpiokZD",
                null)
        );
        um.storeUser(user);
    }

    @AfterTest
    public void tearDown() throws UserManagerException {
        spider = null;
        um.deleteUser(UUID.fromString(USERID));
    }

    @Test
    public void test() throws UserManagerException {
        spider.run();
        User actual = um.getUser(UUID.fromString(USERID));
        Assert.assertNotNull(actual);
        List<Activity> activities = um.getUserActivities(actual.getId());
        Assert.assertTrue(activities.size() > 0);
    }

}
