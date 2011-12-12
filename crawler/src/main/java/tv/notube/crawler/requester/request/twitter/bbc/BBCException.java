package tv.notube.crawler.requester.request.twitter.bbc;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class BBCException extends Throwable {

    public BBCException(String message, Exception e) {
        super(message, e);
    }

    public BBCException(String message) {
        super(message);
    }
}
