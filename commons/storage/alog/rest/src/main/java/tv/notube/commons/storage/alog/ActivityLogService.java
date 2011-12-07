package tv.notube.commons.storage.alog;

import com.sun.jersey.api.core.InjectParam;
import org.joda.time.DateTime;
import tv.notube.commons.storage.model.*;
import tv.notube.commons.storage.model.fields.Field;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

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
    public Response filter(
            @PathParam("owner") String owner,
            @QueryParam("from") Long from,
            @QueryParam("to") Long to,
            @Context UriInfo uriInfo
    ) {
        ActivityLog activityLog = instanceManager.getActivityLog();
        List<String> qParamValue = uriInfo.getQueryParameters().get("q");
        String query;
        if(qParamValue != null) {
            query = qParamValue.get(0);
            return call(from, to, query, owner, activityLog);
        }
        return call(from, to, owner, activityLog);
    }

    private Response call(
            Long from,
            Long to,
            String owner,
            ActivityLog activityLog
    ) {
        DateTime fromDt = getDate(from);
        DateTime toDt = getDate(to);
        Activity[] activities;
        try {
           activities =  activityLog.filter(fromDt, toDt, owner);
        } catch (ActivityLogException e) {
            throw new RuntimeException("Error while calling alog", e);
        }
        return new Response(
                Response.Status.OK,
                "activities found",
                Arrays.asList(activities)
        );
    }

    private Response call(
            Long from,
            Long to,
            String query,
            String owner,
            ActivityLog activityLog
    ) {
        Query queryObj = new Query();
        try {
            query = URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error while URL-decoding query", e);
        }
        try {
            Query.decompile(query, queryObj);
        } catch (QueryException e) {
            throw new RuntimeException("Query is malformed", e);
        }
        DateTime fromDt = getDate(from);
        DateTime toDt = getDate(to);
        Activity[] activities;
        try {
           activities =  activityLog.filter(fromDt, toDt, owner, queryObj);
        } catch (ActivityLogException e) {
            throw new RuntimeException("Error while calling alog", e);
        }
        return new Response(
                Response.Status.OK,
                "activities found",
                Arrays.asList(activities)
        );
    }

    @GET
    @Path("/fields/{id}")
    public Response getFields(
            @PathParam("id") String activityId
    ) {
        ActivityLog activityLog = instanceManager.getActivityLog();
        UUID id = UUID.fromString(activityId);
        Field fields[];
        try {
            fields = activityLog.getFields(id);
        } catch (ActivityLogException e) {
            throw new RuntimeException(
                    "Error while getting field for '" + activityId + "'",
                    e
            );
        }
        return new Response(
                Response.Status.OK,
                "fields for activity '" + activityId + "'",
                Arrays.asList(fields)
        );
    }

    private DateTime getDate(long millisec) {
        return new DateTime(millisec);
    }

}
