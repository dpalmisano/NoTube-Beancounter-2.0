package tv.notube.synch.service;

import tv.notube.synch.core.InMemorySynchronizerImpl;
import tv.notube.synch.core.LoggableSynchronizer;
import tv.notube.synch.core.logger.InMemoryLoggerImpl;
import tv.notube.synch.core.logger.Logger;
import tv.notube.synch.core.priority.InMemoryPriorityManagerImpl;
import tv.notube.synch.core.priority.PriorityManager;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LoaderInstanceManager {

    private static LoaderInstanceManager instance =
            new LoaderInstanceManager();

    private LoggableSynchronizer synchronizer;

    private LoaderInstanceManager() {
        Logger logger = new InMemoryLoggerImpl();
        PriorityManager priorityManager = new InMemoryPriorityManagerImpl(10);
        synchronizer = new InMemorySynchronizerImpl(logger, priorityManager);
    }

    public static LoaderInstanceManager getInstance() {
        if(instance == null) {
            instance = new LoaderInstanceManager();
        }
        return instance;
    }

    public LoggableSynchronizer getSynchronizer() {
        return synchronizer;
    }
}
