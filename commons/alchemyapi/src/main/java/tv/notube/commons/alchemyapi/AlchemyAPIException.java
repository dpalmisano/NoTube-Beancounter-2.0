package tv.notube.commons.alchemyapi;

/**
 * Most generic exception to wrap {@link AlchemyAPI} errors.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AlchemyAPIException extends Exception {

    public AlchemyAPIException(String message, Exception e) {
        super(message, e);
    }
}
