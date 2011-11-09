package tv.notube.crawler.requester;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ServiceResponseException extends Exception  {

    public ServiceResponseException(String message, Exception e) {
        super(message, e);
    }

}
