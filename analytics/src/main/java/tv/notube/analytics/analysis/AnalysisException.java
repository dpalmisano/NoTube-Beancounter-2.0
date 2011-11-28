package tv.notube.analytics.analysis;

import tv.notube.commons.storage.model.ActivityLogException;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AnalysisException extends Exception {

    public AnalysisException(String message, Exception e) {
        super(message, e);
    }
}
