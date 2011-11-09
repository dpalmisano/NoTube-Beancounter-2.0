package tv.notube.profiler.data;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DataManagerException extends Exception {

    public DataManagerException(String message) {
        super(message);
    }

    public DataManagerException(String message, Exception e) {
        super(message, e);
    }
}
