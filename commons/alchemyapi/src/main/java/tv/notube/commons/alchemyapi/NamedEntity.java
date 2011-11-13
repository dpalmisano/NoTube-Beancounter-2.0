package tv.notube.commons.alchemyapi;

import java.net.URI;

/**
 * This class models a generic <i>named entity</i> identifiable from
 * <a href="http://alchemyapi.com">AlchemyAPI</a>.
 *
 * @see <a href="http://www.alchemyapi.com/api/entity/textc.html">Named Entities</a>
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
