package tv.notube.synch.service;

import javax.ws.rs.ext.ExceptionMapper;

/**
 * This class is a basic {@link ExceptionMapper} abstract implementation to handle
 * exception messages.
 *
 * @author Davide Palmisano ( dpalmisano@gmai.com )
 * @param <T>
 */
public abstract class BaseExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

    /**
     * @param e input error.
     * @return a message composed of all the causes of the exception.
     */
    protected String getErrorMessage(Exception e) {
        StringBuilder sb = new StringBuilder();
        Throwable cause = e;
        while (cause != null) {
            sb.append(cause.getMessage());
            cause = cause.getCause();
        }
        return sb.toString();
    }
}