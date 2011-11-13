package tv.notube.commons.alchemyapi;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AlchemyAPIException extends Exception {

    public AlchemyAPIException(String message, Exception e) {
        super(message, e);
    }
}
