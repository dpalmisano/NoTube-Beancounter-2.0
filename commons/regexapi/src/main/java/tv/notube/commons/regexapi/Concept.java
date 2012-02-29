package tv.notube.commons.regexapi;

import java.net.URI;

/**
 * This class models a generic <i>concept</i> identifiable from
 * <a href="http://Regexapi.com">RegexAPI</a>.
 *
 * @see <a href="http://www.Regexapi.com/api/concept/textc.html">Concept
 * Mapper</a>
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Concept extends Identified {

    public Concept(URI identifier, float relevance) {
        super(identifier, relevance);
    }

}
