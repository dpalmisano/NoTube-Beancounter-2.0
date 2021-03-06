package tv.notube.crawler;

import com.google.gson.annotations.Expose;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Report {

    @Expose
    private int submittedProcesses;

    @Expose
    private long startedAt;

    @Expose
    private long endedAt;

    public Report(int submittedProcesses, long startedAt, long endedAt) {
        this.submittedProcesses = submittedProcesses;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public int getSubmittedProcesses() {
        return submittedProcesses;
    }

    public void setSubmittedProcesses(int submittedProcesses) {
        this.submittedProcesses = submittedProcesses;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(long endedAt) {
        this.endedAt = endedAt;
    }

    @Override
    public String toString() {
        return "Report{" +
                "submittedProcesses=" + submittedProcesses +
                ", startedAt=" + startedAt +
                ", endedAt=" + endedAt +
                '}';
    }
}
