package tv.notube.synch.service;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SynchronizerServiceException extends RuntimeException {

    public enum Status {
        WAIT,
        TOKEN,
        ERROR,
        RELEASED, OK, LOCKED
    }

    private Status status;

    private String message;

    public SynchronizerServiceException(
            Status status,
            String message
    ) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
