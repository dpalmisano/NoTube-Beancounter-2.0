package tv.notube.profiler.data;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DataSourceException extends Exception {

    public DataSourceException(String message) {
        super(message);
    }
    
    public DataSourceException(String message, Exception e) {
        super(message, e);
    }

}
