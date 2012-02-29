package tv.notube.synch.core.logger;

import org.joda.time.DateTime;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public final class Release extends Action {

    public Release(String process) {
        super(process);
        super.when = new DateTime();
    }

    public Release(String process, DateTime when) {
        super(process);
        super.when = when;
    }

    public String action() {
        return "release";
    }



}
