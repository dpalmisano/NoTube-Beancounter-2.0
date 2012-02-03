package tv.notube.commons.skos.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Skos {

    private final static String DBPEDIA_SKOS = "http://dbpedia.org/resource/Category:";

    private SkosDao skosDao;

    public Skos(SkosConfiguration skosConfiguration) {
        skosDao = new SkosDao(skosConfiguration.getProperties());
    }

    public List<URI> getSkos(URI resourceUri) throws SkosException {
        String resource = resourceUri
                .toString()
                .substring(
                        "http://dbpedia.org/resource/".length(),
                        resourceUri.toString().length()
                );
        List<String> resourceNames = skosDao.selectSkosByResource(resource);
        List<URI> result = new ArrayList<URI>();
        for(String resourceName : resourceNames) {
            try {
                result.add(new URI(DBPEDIA_SKOS + resourceName));
            } catch (URISyntaxException e) {
                // may happens, just skip
            }
        }
        return result;
    }

}
