package tv.notube.commons.regexapi;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models an <a href="http://Regexapi.com"></a>
 * responses.
 *
 * @see {@link RegexAPI}
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RegexAPIResponse {

    public enum Status {
        OK,
        ERROR
    }

    private Status status;

    private List<Identified> identified = new ArrayList<Identified>();

    public RegexAPIResponse(Status status) {
        this.status = status;
    }

    public RegexAPIResponse(String status) {
        this.status = Status.valueOf(status);
    }

    public Status getStatus() {
        return status;
    }

    public List<Identified> getIdentified() {
        return identified;
    }

    public void setIdentified(List<Identified> identified) {
        this.identified = identified;
    }

    public boolean addIdentified(Identified identified) {
        return this.identified.add(identified);
    }

    @Override
    public String toString() {
        return "RegexAPIResponse{" +
                "status=" + status +
                ", identified=" + identified +
                '}';
    }
}
