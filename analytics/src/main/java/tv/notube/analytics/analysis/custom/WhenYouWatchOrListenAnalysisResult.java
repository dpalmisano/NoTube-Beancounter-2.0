package tv.notube.analytics.analysis.custom;

import org.joda.time.DateTime;
import tv.notube.analytics.analysis.AnalysisResult;

import java.sql.Time;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class WhenYouWatchOrListenAnalysisResult extends AnalysisResult {

    private int[] slice;

    private int total;

    public WhenYouWatchOrListenAnalysisResult(DateTime dateTime, int[] slice) {
        super(dateTime);
        this.slice = slice;
        total = 0;
        for(int i=0; i < slice.length; i++) {
            total += slice[i];
        }
    }

    public double getPercentages(Integer slice) {
        if(total == 0) {
            return 0.0;
        }
        return this.slice[slice] / (double) total * 100;
    }

    public int getNumbers(Integer slice) {
        return this.slice[slice];
    }

    public TimeSlice getSlice() {
        double[] percentage = new double[4];
        for(int i=0; i < percentage.length; i++) {
            if(total == 0) {
                percentage[i] = 0.0;
            } else {
                percentage[i] = slice[i] / (double) total * 100;
            }
        }
        return new TimeSlice(percentage);
    }

}
