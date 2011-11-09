package tv.notube.extension.profilingline.lupedia.http.json;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LupediaResponse {

    private List<LupediaEntry> entries = new ArrayList<LupediaEntry>();

    public List<LupediaEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<LupediaEntry> entries) {
        this.entries = entries;
    }

    public boolean addEntry(LupediaEntry entry) {
        return entries.add(entry);
    }

    public List<URI> getEntriesAsURIs() {
        List<URI> result = new ArrayList<URI>();
        for(LupediaEntry entry : entries) {
            try {
                result.add(new URI(entry.getInstanceUri()));
            } catch (URISyntaxException e) {
                // just skip it
                continue;
            }
        }
        return result;
    }

}
