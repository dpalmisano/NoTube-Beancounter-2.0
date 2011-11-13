package tv.notube.commons.alchemyapi;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models an <a href="http://alchemyapi.com"></a>
 * responses.
 *
 * @see {@link AlchemyAPI}
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AlchemyAPIResponse {

    public enum Status {
        OK,
        ERROR
    }

    private Status status;

    private List<Identified> identified = new ArrayList<Identified>();

    public AlchemyAPIResponse(Status status) {
        this.status = status;
    }

    public AlchemyAPIResponse(String status) {
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
        return "AlchemyAPIResponse{" +
                "status=" + status +
                ", identified=" + identified +
                '}';
    }
}
