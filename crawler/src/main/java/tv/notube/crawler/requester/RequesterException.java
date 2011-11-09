package tv.notube.crawler.requester;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RequesterException extends Exception {

    public RequesterException(String message, Exception e) {
        super(message, e);
    }

}
