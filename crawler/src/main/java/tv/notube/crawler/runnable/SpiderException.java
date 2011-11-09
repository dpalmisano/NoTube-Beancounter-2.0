package tv.notube.crawler.runnable;

/**
 * @author Davide Palmisano (dpalmisano@gmail.com)
 */
public class SpiderException extends Exception {

    public SpiderException(String message, Exception e) {
        super(message, e);
    }
}
