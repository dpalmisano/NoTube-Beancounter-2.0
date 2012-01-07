package tv.notube.analytics.analysis.custom;

import tv.notube.analytics.analysis.*;
import tv.notube.commons.storage.model.ActivityLog;

import java.util.HashSet;
import java.util.Set;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class WhenYouWatchOrListenAnalysis extends StorageAnalysis {

    public WhenYouWatchOrListenAnalysis(ActivityLog alog, String name, String description) {
        super(alog, name, description);
    }

    public AnalysisResult run(String owner) throws AnalysisException {
        throw new UnsupportedOperationException("NIY");
    }

    public AnalysisDescription getAnalysisDescription() {
        MethodDescription getStatistics;
        getStatistics = new MethodDescription(
                "getTimeSlice",
                "this method returns activity statistics day by day of the last month",
                new String[] { "java.lang.Integer" }
        );
        Set<MethodDescription> tfdMds = new HashSet<MethodDescription>();
        tfdMds.add(getStatistics);
        return new AnalysisDescription(
                getName(),
                getDescription(),
                getQuery(),
                TimeFrameAnalysis.class.getCanonicalName(),
                TimeFrameAnalysisResult.class.getCanonicalName(),
                tfdMds
        );
    }
}
