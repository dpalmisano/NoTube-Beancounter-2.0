package tv.notube.commons.regexapi;

/**
 * Most generic exception to wrap {@link RegexAPI} errors.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RegexAPIException extends Exception {

    public RegexAPIException(String message, Exception e) {
        super(message, e);
    }
}
