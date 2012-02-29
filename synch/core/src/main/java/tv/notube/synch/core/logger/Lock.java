package tv.notube.synch.core.logger;

import org.joda.time.DateTime;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public final class Lock extends Action {

    public Lock(String process) {
        super(process);
        super.when = new DateTime();
    }

    public Lock(String process, DateTime when) {
        super(process);
        super.when = when;
    }

    @Override
    public String action() {
        return "lock";
    }

}
