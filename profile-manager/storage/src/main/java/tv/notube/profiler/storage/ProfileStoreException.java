package tv.notube.profiler.storage;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProfileStoreException extends Exception {
    
    public ProfileStoreException(String message, Exception e) {
        super(message, e);
    }

    public ProfileStoreException(String message) {
        super(message);
    }
}
