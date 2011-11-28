package tv.notube.commons.storage.alog;

import com.sun.jersey.api.core.InjectParam;
import tv.notube.commons.storage.model.ActivityLog;
import tv.notube.commons.storage.model.Query;
import tv.notube.commons.storage.model.QueryException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Path("/activities")
@Produces(MediaType.APPLICATION_JSON)
public class ActivityLogService {

    @InjectParam
    private InstanceManager instanceManager;

    @GET
    @Path("/{owner}")
    public void m(
            @PathParam("owner") String owner,
            @QueryParam("from") String from,
            @QueryParam("to") String to,
            @QueryParam("q") String query
    ) {
        ActivityLog activityLog = instanceManager.getActivityLog();
        Query queryObj = new Query();
        try {
            Query.decompile(query, queryObj);
        } catch (QueryException e) {
            throw new RuntimeException("Query is malformed", e);
        }
    }

}
