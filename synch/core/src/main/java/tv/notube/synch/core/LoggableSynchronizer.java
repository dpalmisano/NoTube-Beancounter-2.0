package tv.notube.synch.core;

import tv.notube.synch.core.logger.Logger;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class LoggableSynchronizer implements Synchronizer {

    protected Logger logger;

    protected LoggableSynchronizer(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }
}
