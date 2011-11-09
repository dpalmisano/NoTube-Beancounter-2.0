package tv.notube.extension.profilingline.lupedia;

import java.net.URI;
import java.util.List;

/**
 * @autor Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Lupedia {

    List<URI> getResources(String text) throws LupediaException;

}
