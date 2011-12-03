package tv.notube.analytics.analysis;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AnalysisResult implements Serializable {

    static final long serialVersionUID = 10185559472137495L;

    private DateTime executedAt;

    private String name;

    private String user;

    public AnalysisResult(DateTime dateTime) {
        executedAt = dateTime;
    }

    public DateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(DateTime executedAt) {
        this.executedAt = executedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
