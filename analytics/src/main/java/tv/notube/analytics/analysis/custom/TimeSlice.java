package tv.notube.analytics.analysis.custom;

import com.google.gson.annotations.Expose;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TimeSlice {

    @Expose
    private double slice0;

    @Expose
    private double slice1;

    @Expose
    private double slice2;

    @Expose
    private double slice3;

    public TimeSlice(double[] slice) {
        this.slice0 = slice[0];
        this.slice1 = slice[1];
        this.slice2 = slice[2];
        this.slice3 = slice[3];
    }

    public double getSlice0() {
        return slice0;
    }

    public double getSlice1() {
        return slice1;
    }

    public double getSlice2() {
        return slice2;
    }

    public double getSlice3() {
        return slice3;
    }

    @Override
    public String toString() {
        return "TimeSlice{" +
                "slice0=" + slice0 +
                ", slice1=" + slice1 +
                ", slice2=" + slice2 +
                ", slice3=" + slice3 +
                '}';
    }
}
