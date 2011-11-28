package tv.notube.analytics;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AnalyzerException extends Exception {

    public AnalyzerException(String message) {
        super(message);
    }

    public AnalyzerException(String message, Exception e) {
        super(message, e);
    }
}
