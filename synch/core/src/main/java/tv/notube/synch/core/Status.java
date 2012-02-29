package tv.notube.synch.core;

import org.joda.time.DateTime;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class Status {

    protected DateTime when;

    private String who;

    public Status(String process) {
        this.who = process;
    }

    public DateTime getWhen() {
        return when;
    }

    public void setWhen(DateTime when) {
        this.when = when;
    }

    public String getWho() {
        return who;
    }

    public abstract String status();

    @Override
    public String toString() {
        return "Status{" +
                "when=" + when +
                ", who='" + who + '\'' +
                '}';
    }
}
