package tv.notube.synch.core.logger;

import org.joda.time.DateTime;
import tv.notube.synch.model.logger.Action;
import tv.notube.synch.model.logger.Lock;
import tv.notube.synch.model.logger.Release;

import java.util.LinkedList;
import java.util.List;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class InMemoryLoggerImpl implements Logger {

    private final static int SIZE = 100;

    private List<Action> activities = new LinkedList<Action>();

    public void locked(String who, DateTime when) throws LoggerException {
        synchronized (this) {
            if (activities.size() == SIZE) {
                flush();
            }
        }
        Action lock = new Lock(who, when);
        activities.add(lock);
    }

    private void flush() {
        activities = null;
        activities = new LinkedList<Action>();
    }

    public void released(String who, DateTime when) throws LoggerException {
        synchronized (this) {
            if (activities.size() == SIZE) {
                flush();
            }
        }
        Action release = new Release(who, when);
        activities.add(release);
    }

    public Action getLatest() throws LoggerException {
        if(activities.size() == 0) {
            return null;
        }
        return activities.get(activities.size() - 1);
    }

    public Action[] getLog() throws LoggerException {
        return activities.toArray(new Action[activities.size()]);
    }
}
