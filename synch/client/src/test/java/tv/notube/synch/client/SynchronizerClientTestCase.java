package tv.notube.synch.client;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.synch.model.Locked;
import tv.notube.synch.model.Released;
import tv.notube.synch.model.Status;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SynchronizerClientTestCase {

    private final String URL = "http://localhost:8080/service-1.0-SNAPSHOT/rest/synch";

    private SynchronizerClient client;

    @BeforeTest
    public void setUp() {
        client = new SynchronizerClient(URL);
    }

    @Test
    public void testWholeProcess() throws SynchronizerClientException {
        Status status = client.getStatus();
        Assert.assertNotNull(status);
        Assert.assertTrue(status instanceof Released);

        final String PROCESS = "test-process";

        UUID token = client.getToken(PROCESS);
        Assert.assertNotNull(token);

        Response response = client.lock(PROCESS, token);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), Response.Status.OK);

        status = client.getStatus();
        Assert.assertNotNull(status);
        Assert.assertTrue(status instanceof Locked);
        Assert.assertEquals(status.getWho(), PROCESS);

        client.release(PROCESS, token);

        status = client.getStatus();
        Assert.assertNotNull(status);
        Assert.assertTrue(status instanceof Released);
        Assert.assertEquals(status.getWho(), PROCESS);
    }

}
