package tv.notube.synch.core.priority;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InMemoryPriorityManagerImplTestCase {

    private PriorityManager priorityManager;

    @BeforeTest
    public void setUp() {
        priorityManager = new InMemoryPriorityManagerImpl(100);
    }

    @Test
    public void singleHandShake() throws PriorityManagerException {
        final String process = "test-process";
        UUID token = priorityManager.askToken(process);
        UUID actual = priorityManager.next();
        Assert.assertEquals(actual, token);
        // again
        actual = priorityManager.next();
        Assert.assertEquals(actual, token);

        // and again
        actual = priorityManager.next();
        Assert.assertEquals(actual, token);

        // now, revoke it
        priorityManager.revokeToken(process);
        actual = priorityManager.next();
        Assert.assertNull(actual);
    }

}
