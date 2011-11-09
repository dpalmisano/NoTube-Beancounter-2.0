package tv.notube.extension.profilingline.skos.http;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SkosLookupResponse {

    private List<URI> skos = new ArrayList<URI>();

    public List<URI> getSkos() {
        return skos;
    }

    public void setSkos(List<URI> skos) {
        this.skos = skos;
    }

    public void addSkos(URI uri) {
        skos.add(uri);
    }
}
