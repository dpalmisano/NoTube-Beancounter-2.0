package tv.notube.analytics.analysis.custom;

import org.joda.time.DateTime;
import tv.notube.analytics.analysis.AnalysisResult;

import java.util.HashMap;
import java.util.Map;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TimeFrameAnalysisResult extends AnalysisResult {

    private Map<Integer, ActivityAnalysisResult> activityAnalysisResults =
            new HashMap<Integer, ActivityAnalysisResult>();

    public TimeFrameAnalysisResult(DateTime dateTime) {
        super(dateTime);
    }

    protected void addAnalysis(int day, ActivityAnalysisResult aar) {
        activityAnalysisResults.put(day, aar);
    }

    public ActivityAnalysisResult getStatistics(Integer day) {
        if(day > 31 || day < 1) {
            throw new IllegalArgumentException(
                    "day parameter must be a day of a month"
            );
        }
        return activityAnalysisResults.get(day);
    }

}
