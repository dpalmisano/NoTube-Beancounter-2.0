package tv.notube.kvs.storage.serialization;

import java.io.Serializable;
import java.net.URI;

public class TestClassToBeSerialized implements Serializable {

    private static final long serialVersionUID = 3487435845519393L;

    private URI[] uris;

    private int index;

    public TestClassToBeSerialized() {
        index = 0;
        uris = new URI[10];
    }

    public void addURI(URI uri) {
        if (index >= uris.length) {
            throw new RuntimeException("Not enough space");
        }
        uris[index] = uri;
        index++;
    }

    public URI[] getURIs() {
        return uris;
    }
}