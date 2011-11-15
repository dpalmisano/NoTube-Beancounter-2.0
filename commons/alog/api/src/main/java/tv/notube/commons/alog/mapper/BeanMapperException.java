package tv.notube.commons.alog.mapper;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class BeanMapperException extends Exception {

    public BeanMapperException(String message) {
        super(message);
    }

    public BeanMapperException(String message, Exception e) {
        super(message, e);
    }

}
