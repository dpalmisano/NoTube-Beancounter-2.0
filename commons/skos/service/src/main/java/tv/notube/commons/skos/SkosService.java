package tv.notube.commons.skos;

import com.sun.jersey.api.core.InjectParam;
import tv.notube.commons.skos.service.Skos;
import tv.notube.commons.skos.service.SkosException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Path("/skos")
@Produces(MediaType.APPLICATION_JSON)
public class SkosService {

    private final static String DBPEDIA = "http://dbpedia.org/resource/";

    @InjectParam
    private InstanceManager instanceManager;

    @GET
    @Path("/{resource}")
    public List<URI> getSkos(
            @PathParam("resource") String resource
    ) {
        Skos skos = instanceManager.getSkos();
        try {
            return skos.getSkos(new URI(DBPEDIA + resource));
        } catch (SkosException e) {
            throw new RuntimeException(
                    "Error while getting SKOS for '" + resource + "'", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(
                    "Error while building resource URI for '" + resource + "'",
                    e);
        }
    }

}
