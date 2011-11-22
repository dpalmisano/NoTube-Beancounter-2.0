package tv.notube.commons.skos.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Skos {

    private final static String DBPEDIA_SKOS = "http://dbpedia.org/resource/Category:";

    private SkosDao skosDao;

    public Skos() {
        // TODO (high) make this configuarable
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:mysql://moth.notube.tv:3306/beancounter");
        properties.setProperty("username", "notube");
        properties.setProperty("password", "notubepass");
        skosDao = new SkosDao(properties);
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
