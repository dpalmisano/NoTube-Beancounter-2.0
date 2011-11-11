package tv.notube.commons.alog;

import junit.framework.Assert;
import org.joda.time.DateTime;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultActivityLogImplTestCase {

    private ActivityLog activityLog;

    @BeforeTest
    public void setUp() {
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:mysql://127.0.0.1:3306/alog");
        properties.setProperty("username", "alog");
        properties.setProperty("password", "alog");
        activityLog = new DefaultActivityLogImpl(properties);
    }

    @AfterTest
    public void tearDown() throws ActivityLogException {
        activityLog.delete("test-owner");
    }

    @Test
    public void simpleTest() throws ActivityLogException {
        activityLog.delete("test-owner");
        DateTime before = new DateTime();
        for(int i=0; i < 10; i++) {
            IntegerField index = new IntegerField("index", i);
            Field fields[] = { index };
            activityLog.log("test-owner", "just a test activity", fields);
        }
        DateTime now = new DateTime();
        Activity activities[] = activityLog.filter(before, now);
        Assert.assertEquals(activities.length, 10);

        activities = activityLog.filter(before, now, "test-owner");
        Assert.assertEquals(activities.length, 10);

        for(Activity activity : activities) {
            Field[] fields = activityLog.getFields(activity.getId());
            Assert.assertEquals(fields.length, 1);
        }

        activityLog.delete("test-owner");
        activities = activityLog.filter(before, now);
        Assert.assertEquals(activities.length, 0);
    }

    @Test
    public void testDeleteByDateRange() throws ActivityLogException, InterruptedException {
        DateTime before = new DateTime();
        for (int i = 0; i < 10; i++) {
            IntegerField index = new IntegerField("index", i);
            Field fields[] = {index};
            activityLog.log("test-owner", "just a test activity", fields);
        }
        Thread.sleep(1000);
        DateTime after = new DateTime();
        Activity activities[] = activityLog.filter(before, after,
                "test-owner");
        Assert.assertEquals(activities.length, 10);

        activityLog.delete(before, after);
        activities = activityLog.filter(before, after,
                "test-owner");
        Assert.assertEquals(activities.length, 0);
    }

    @Test
    public void testDeleteByDateRangeAndOwner() throws ActivityLogException {
        DateTime before = new DateTime();
        for (int i = 0; i < 10; i++) {
            IntegerField index = new IntegerField("index", i);
            Field fields[] = {index};
            activityLog.log("test-owner", "just a test activity", fields);
        }
        DateTime after = new DateTime();
        Activity activities[] = activityLog.filter(before, after,
                "test-owner");
        Assert.assertEquals(activities.length, 10);

        activityLog.delete(before, after, "test-owner");
        activities = activityLog.filter(before, after,
                "test-owner");
        Assert.assertEquals(activities.length, 0);
    }

}