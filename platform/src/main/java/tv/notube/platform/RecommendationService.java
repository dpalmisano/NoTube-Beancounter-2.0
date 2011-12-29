package tv.notube.platform;

import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Random;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Path("/recommendation")
@Produces(MediaType.APPLICATION_JSON)
public class RecommendationService {

    @InjectParam
    private InstanceManager instanceManager;

    @POST
    @Path("/similarity/{username}")
    public PlatformResponse getSimilarityIndex(
            @PathParam("username") String username,
            @FormParam("nic") String nic
    ) {
        Random generator = instanceManager.getRecommender();
        // TODO (low) this class is a mockup mainly
        double similarity = generator.nextDouble();

        return new PlatformResponse(
                PlatformResponse.Status.OK,
                "similarity index for '" + username + "'",
                similarity
        );
    }
}
