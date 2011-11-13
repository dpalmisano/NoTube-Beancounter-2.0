package tv.notube.commons.alchemyapi;

import java.net.URI;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class NamedEntity extends Identified {

    private String type;

    public NamedEntity(URI identifier, float relevance) {
        super(identifier, relevance);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NamedEntity{" +
                "type='" + type + '\'' +
                "} " + super.toString();
    }
}
