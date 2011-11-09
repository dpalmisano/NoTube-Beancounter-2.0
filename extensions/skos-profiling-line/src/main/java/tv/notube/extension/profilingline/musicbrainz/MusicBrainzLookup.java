package tv.notube.extension.profilingline.musicbrainz;

import org.sameas.sameas4j.DefaultSameAsServiceFactory;
import org.sameas.sameas4j.Equivalence;
import org.sameas.sameas4j.SameAsService;
import org.sameas.sameas4j.SameAsServiceException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class MusicBrainzLookup {

    private SameAsService sameAsService;

    private final static String BBC = "http://www.bbc.co.uk/music/artists/";

    public MusicBrainzLookup() {
        sameAsService = DefaultSameAsServiceFactory.getSingletonInstance();
    }

    public URI resolve(String mbid) throws MusicBrainzLooupException {
        Equivalence eq;
        try {
            eq = sameAsService.getDuplicates(new URI(BBC + mbid));
        } catch (SameAsServiceException e) {
            throw new MusicBrainzLooupException("Error while calling the Sameas.org service", e);
        } catch (URISyntaxException e) {
            throw new MusicBrainzLooupException("URI is not well formed", e);
        }
        for(URI uri : eq) {
            if(uri.getAuthority().equals("dbpedia.org")) {
                return uri;
            }
        }
        throw new MusicBrainzNotResolvableException("Not resolvable mbid");
    }

}
