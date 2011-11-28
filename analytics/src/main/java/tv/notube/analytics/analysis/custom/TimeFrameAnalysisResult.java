package tv.notube.analytics.analysis.custom;

import org.joda.time.DateTime;
import tv.notube.analytics.analysis.AnalysisResult;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TimeFrameAnalysisResult extends AnalysisResult {

    private Map<Integer, ActivityAnalysisResult> activityAnalysisResults =
            new HashMap<Integer, ActivityAnalysisResult>();

    protected void addAnalysis(int day, ActivityAnalysisResult aar) {
        activityAnalysisResults.put(day, aar);
    }

    public ActivityAnalysisResult getStatistics(int day) {
        if(day > 31 || day < 1) {
            throw new IllegalArgumentException(
                    "day parameter must be a day of a month"
            );
        }
        return activityAnalysisResults.get(day);
    }

}
