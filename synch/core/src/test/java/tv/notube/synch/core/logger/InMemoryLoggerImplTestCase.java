package tv.notube.synch.core.logger;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InMemoryLoggerImplTestCase {

    private Logger logger;

    @BeforeTest
    public void setUp() {
        logger = new InMemoryLoggerImpl();
    }

    @Test
    public void singleThreadTest() throws LoggerException {
        final String processName = "process-";
        for(int i=0; i < 150; i++) {
            logger.locked(processName + i, new DateTime());
            Action action =  logger.getLatest();
            Assert.assertNotNull(action);
            Assert.assertEquals(action.getWho(), processName + i);
            Assert.assertEquals(action.action(), "lock");
            logger.released(processName + i, new DateTime());
        }
        Action latest = logger.getLatest();
        Assert.assertEquals(latest.action(), "release");
        Assert.assertEquals(latest.getWho(), processName + "149");
    }
}
