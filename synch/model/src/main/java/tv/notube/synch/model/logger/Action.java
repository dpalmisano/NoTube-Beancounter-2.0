package tv.notube.synch.model.logger;

import org.joda.time.DateTime;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class Action {

    protected DateTime when;

    private String who;

    public Action(String process) {
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

    public abstract String action();

    @Override
    public String toString() {
        return "Action{" +
                "when=" + when +
                ", who='" + who + '\'' +
                '}';
    }
}
