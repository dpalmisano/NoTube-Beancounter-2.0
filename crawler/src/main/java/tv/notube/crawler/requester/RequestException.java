package tv.notube.crawler.requester;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RequestException extends Exception {

    public RequestException(String message, Exception e) {
        super(message, e);
    }
}
