package tv.notube.synch.core;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.synch.core.logger.InMemoryLoggerImpl;
import tv.notube.synch.core.logger.Logger;
import tv.notube.synch.core.priority.InMemoryPriorityManagerImpl;
import tv.notube.synch.core.priority.PriorityManager;
import tv.notube.synch.core.priority.PriorityManagerException;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InMemorySynchronizerImplTestCase {

    private Synchronizer synchronizer;

    private final int SIZE = 100;

    @BeforeTest
    public void setUp() {
        Logger logger = new InMemoryLoggerImpl();
        PriorityManager priorityManager = new InMemoryPriorityManagerImpl(SIZE);
        synchronizer = new InMemorySynchronizerImpl(logger, priorityManager);
    }

    @Test
    public void testSingleThread() throws PriorityManagerException, SynchronizerException {
        final String process1 = "test-process-1";
        try {
            synchronizer.lock(process1, UUID.randomUUID());
        } catch (SynchronizerException e) {
            Assert.assertTrue(true);
        }

        UUID process1Token = synchronizer.getToken(process1);
        synchronizer.lock(process1, process1Token);
        Assert.assertEquals(synchronizer.getStatus().status(), "locked");

        final String process2 = "test-process-2";
        UUID process2Token = synchronizer.getToken(process2);
        try {
            synchronizer.lock(process2, process2Token);
        } catch (SynchronizerException e) {
            Assert.assertTrue(true);
        }

        synchronizer.release(process1, process1Token);
        Assert.assertEquals(
                synchronizer.getStatus().status(),
                "released"
        );

        final String process3 = "test-process-3";
        UUID process3Token = synchronizer.getToken(process3);

        synchronizer.lock(process2, process2Token);
        Assert.assertEquals(synchronizer.getStatus().status(), "locked");

        try {
            synchronizer.lock(process3, process3Token);
        } catch (SynchronizerException e) {
            Assert.assertTrue(true);
        }
        synchronizer.release(process2, process2Token);
        Assert.assertEquals(
                synchronizer.getStatus().status(),
                "released"
        );

        synchronizer.lock(process3, process3Token);
        synchronizer.release(process3, process3Token);
    }

    @Test
    public void testMultiThread() {
        Thread[] threads = new Thread[SIZE];
        FakeProcess[] processes = new FakeProcess[SIZE];
        for (int i = 0; i < SIZE; i++) {
            FakeProcess fakeProcess = new FakeProcess(synchronizer, "fake-process-" + i);
            processes[i] = fakeProcess;
            Thread worker = new Thread(fakeProcess);
            threads[i] = worker;
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        wait(threads);
        int success = 0, fail = 0;
        for (FakeProcess fakeProcess : processes) {
            if (!fakeProcess.isSuccess()) {
                fail++;
            } else {
                success++;
            }
        }
        Assert.assertEquals(fail, 0);
        Assert.assertEquals(success, SIZE);
        Assert.assertEquals(Counter.getCount(), SIZE);
    }

    private void wait(Thread[] threads) {
        int running;
		do {
			running = 0;
			for (Thread thread : threads) {
				if (thread.isAlive()) {
					running++;
				}
			}
		} while (running > 0);
    }

}
