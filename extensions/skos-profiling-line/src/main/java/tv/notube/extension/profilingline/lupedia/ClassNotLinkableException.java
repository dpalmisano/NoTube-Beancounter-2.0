package tv.notube.extension.profilingline.lupedia;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ClassNotLinkableException extends LupediaException {

    public ClassNotLinkableException(String message) {
        super(message);
    }

    public ClassNotLinkableException(String message, Exception e) {
        super(message, e);
    }
}
