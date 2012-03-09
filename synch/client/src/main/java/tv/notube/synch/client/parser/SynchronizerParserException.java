package tv.notube.synch.client.parser;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SynchronizerParserException extends Exception {

    public SynchronizerParserException(String message) {
        super(message);
    }

    public SynchronizerParserException(String message, Exception e) {
        super(message, e);
    }

}
