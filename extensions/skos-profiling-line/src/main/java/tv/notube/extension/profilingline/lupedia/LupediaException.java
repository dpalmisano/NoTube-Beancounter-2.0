package tv.notube.extension.profilingline.lupedia;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LupediaException extends Exception {

    public LupediaException(String message, Exception e) {
        super(message, e);
    }

    public LupediaException(String message) {
    }
}
