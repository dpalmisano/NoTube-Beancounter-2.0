package tv.notube.platform;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines the result of a processing.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Produces(MediaType.APPLICATION_JSON)
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Response {

    public enum Status {
        OK,
        NOK
    }

    private Status status;

    private String message;

    private Object object;

    public Response(){}

    public Response(Status s, String m) {
        status = s;
        message = m;
    }

    public Response(Status s, String m, Object o) {
        status = s;
        message = m;
        object = o;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}