package tv.notube.synch.model;

import org.joda.time.DateTime;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public final class Locked extends Status {

    public Locked(String process) {
        super(process);
        super.when = new DateTime();
    }

    public Locked(String process, DateTime when) {
        super(process);
        super.when = when;
    }

    @Override
    public String status() {
        return "locked";
    }

}
