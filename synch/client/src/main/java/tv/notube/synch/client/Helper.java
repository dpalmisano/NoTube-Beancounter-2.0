package tv.notube.synch.client;

import tv.notube.synch.model.Locked;
import tv.notube.synch.model.Status;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Helper {

    private static Helper instance;

    public synchronized static Helper getInstance(String url) {
        if(instance == null) {
            instance = new Helper(url);
        }
        return instance;
    }

    private SynchronizerClient client;

    private Helper(String url) {
        this.client = new SynchronizerClient(url);
    }

    public UUID access(String process) throws SynchronizerClientException {
        UUID token;
        token = client.getToken(process);
        Status st;
        st = client.getStatus();
        while ((st instanceof Locked)) {
            // just keeping ask the status
            st = client.getStatus();
        }

        Response r;
        r = client.lock(process, token);

        while (!r.getStatus().equals(Response.Status.OK)) {
            // just keep asking for lock
            r = client.lock(process, token);
        }
        // ok, you got it - let's go out
        return token;
    }

    public void release(String process, UUID token) throws SynchronizerClientException {
        Response r;
        r = client.release(process, token);

        while (r.getStatus().equals(Response.Status.WAIT)) {
            // just keep asking for release
            r = client.release(process, token);
        }
        // ok, you got it - let's go out
    }
}
