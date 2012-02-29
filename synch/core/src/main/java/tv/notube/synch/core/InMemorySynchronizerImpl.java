package tv.notube.synch.core;

import org.joda.time.DateTime;
import tv.notube.synch.core.logger.Logger;
import tv.notube.synch.core.logger.LoggerException;
import tv.notube.synch.core.priority.PriorityManager;
import tv.notube.synch.core.priority.PriorityManagerException;

import java.util.*;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InMemorySynchronizerImpl extends LoggableSynchronizer {

    private static final String NAME = "synchronizer";

    private PriorityManager priorityManager;

    private Status status = new Released(NAME);

    protected InMemorySynchronizerImpl(Logger logger, PriorityManager priorityManager) {
        super(logger);
        this.priorityManager = priorityManager;
    }

    public synchronized UUID getToken(String process) throws SynchronizerException {
        try {
            return priorityManager.askToken(process);
        } catch (PriorityManagerException e) {
            final String errMsg = "Error while asking token for process [" + process + "]";
            throw new SynchronizerException(errMsg, e);
        }
    }

    public synchronized void lock(String process, UUID token) throws
            SynchronizerException {
        if(!status.status().equals("released")) {
            final String errMsg = "Already locked by [" + status.getWho() +
                    "] at [" + status.getWhen() + "]";
            throw new SynchronizerException(errMsg);
        }
        UUID candidate;
        try {
            candidate = priorityManager.next();
        } catch (PriorityManagerException e) {
            final String errMsg = "Error while asking who's the next candidate token";
            throw new SynchronizerException(errMsg, e);
        }
        if(candidate == null || !candidate.equals(token)) {
            final String errMsg = "It's not the turn of process [" + process + "]";
            throw new SynchronizerException(errMsg);
        }
        try {
            priorityManager.revokeToken(process);
        } catch (PriorityManagerException e) {
            final String errMsg = "It's not the turn of process [" + process + "]";
            throw new SynchronizerException(errMsg);
        }
        status = new Locked(process);
        try {
            logger.locked(process, new DateTime());
        } catch (LoggerException e) {
            final String errMsg = "Error while logging process [" + process + "]";
            throw new SynchronizerException(errMsg, e);
        }
    }

    public synchronized void release(String process, UUID token) throws SynchronizerException {
        if(!status.status().equals("locked") && !status.status().equals(NAME)) {
            final String errMsg = "Already released by [" + status.getWho() +
                    "] at [" + status.getWhen() + "]";
            throw new SynchronizerException(errMsg);
        }
        if(!status.getWho().equals(process)) {
            final String errMsg = "Process [" + status.getWho() + "] does not" +
                    " have rights to release it";
            throw new SynchronizerException(errMsg);
        }
        try {
            priorityManager.revokeToken(process);
        } catch (PriorityManagerException e) {
            final String errMsg = "It's not the turn of process [" + process + "]";
            throw new SynchronizerException(errMsg);
        }
        status = new Released(process);
        try {
            logger.released(process, new DateTime());
        } catch (LoggerException e) {
            final String errMsg = "Error while logging process [" + process + "]";
            throw new SynchronizerException(errMsg, e);
        }
    }

    public Status getStatus() throws SynchronizerException {
        return status;
    }
}
