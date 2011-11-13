package tv.notube.commons.alchemyapi;

import java.net.URI;

/**
 * This class models a generic <i>concept</i> identifiable from
 * <a href="http://alchemyapi.com">AlchemyAPI</a>.
 *
 * @see <a href="http://www.alchemyapi.com/api/concept/textc.html">Concept
 * Mapper</a>
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Concept extends Identified {

    public Concept(URI identifier, float relevance) {
        super(identifier, relevance);
    }

}
