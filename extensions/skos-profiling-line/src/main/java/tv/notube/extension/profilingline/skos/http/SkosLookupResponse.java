package tv.notube.extension.profilingline.skos.http;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SkosLookupResponse {

    private List<URI> uris = new ArrayList<URI>();

    public List<URI> getUris() {
        return uris;
    }

    public void setUris(List<URI> uris) {
        this.uris = uris;
    }

    public void addUri(URI uri) {
        uris.add(uri);
    }
}
