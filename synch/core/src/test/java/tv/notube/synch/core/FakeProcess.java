package tv.notube.synch.core;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class FakeProcess implements Runnable {

    private Synchronizer synchronizer;

    private String name;

    private boolean success = false;

    public FakeProcess(Synchronizer synchronizer, String name) {
        this.synchronizer = synchronizer;
        this.name = name;
    }

    public void run() {
        UUID token;
        try {
            token = synchronizer.getToken(name);
        } catch (SynchronizerException e) {
            throw new RuntimeException("Error while getting token", e);
        }
        boolean success = false;
        while (!success) {
            try {
                synchronizer.lock(name, token);
            } catch (SynchronizerException e) {
                success = false;
                continue;
            }
            success = true;
        }
        // access to the protected resource
        Counter.increment();
        success = false;
        while (!success) {
            try {
                synchronizer.release(name, token);
            } catch (SynchronizerException e) {
                success = false;
                continue;
            }
            success = true;
        }
        // woot! I successfully used it
        this.success = true;
    }

    public boolean isSuccess() {
        return success;
    }

}
