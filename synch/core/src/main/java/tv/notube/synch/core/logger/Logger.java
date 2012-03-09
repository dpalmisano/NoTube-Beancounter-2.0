package tv.notube.synch.core.logger;

import org.joda.time.DateTime;
import tv.notube.synch.model.logger.Action;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Logger {

    public void locked(String who, DateTime when) throws LoggerException;

    public void released(String who, DateTime when) throws LoggerException;

    public Action getLatest() throws LoggerException;

    public Action[] getLog() throws LoggerException;

}
