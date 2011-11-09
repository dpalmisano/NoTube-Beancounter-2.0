package tv.notube.crawler;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class CrawlerException extends Exception {

    public CrawlerException(String message, Exception e) {
        super(message, e);
    }
}
