package tv.notube.commons.configuration.analytics;

import tv.notube.commons.configuration.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AnalyticsConfiguration extends Configuration {

    private Set<AnalysisDescription> analysisDescriptions =
            new HashSet<AnalysisDescription>();

    public AnalyticsConfiguration(Set<AnalysisDescription> analysisDescriptions) {
        this.analysisDescriptions = analysisDescriptions;
    }

    public void addAnalysisDescription(AnalysisDescription analysisDescription) {
        this.analysisDescriptions.add(analysisDescription);
    }

    public Set<AnalysisDescription> getAnalysisDescriptions() {
        return analysisDescriptions;
    }

    @Override
    public String toString() {
        return "AnalyticsConfiguration{" +
                "analysisDescriptions=" + analysisDescriptions +
                "} " + super.toString();
    }
}
