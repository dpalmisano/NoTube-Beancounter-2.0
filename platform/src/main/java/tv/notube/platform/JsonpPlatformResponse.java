package tv.notube.platform;

import com.google.gson.annotations.Expose;

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
@Produces("application/x-javascript")
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class JsonpPlatformResponse {

    public enum Status {
        OK,
        NOK
    }

    @Expose
    private Status status;

    @Expose
    private String message;

    @Expose
    private Object object;

    public JsonpPlatformResponse(){}

    public JsonpPlatformResponse(Status s, String m) {
        status = s;
        message = m;
    }

    public JsonpPlatformResponse(Status s, String m, Object o) {
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