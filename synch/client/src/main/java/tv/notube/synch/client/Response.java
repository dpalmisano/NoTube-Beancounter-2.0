package tv.notube.synch.client;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Response {

    public enum Status {
        WAIT,
        TOKEN,
        ERROR,
        RELEASED,
        OK,
        LOCKED
    }

    private Status status;

    private String message;

    public Response(
            String status,
            String message
    ) {
        this.status = Status.valueOf(status);
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
