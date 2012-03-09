package tv.notube.synch.model;

import org.joda.time.DateTime;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public final class Released extends Status {

    public Released(String process) {
        super(process);
        super.when = new DateTime();
    }

    public Released(String process, DateTime when) {
        super(process);
        super.when = when;
    }

    @Override
    public String status() {
        return "released";
    }

}
