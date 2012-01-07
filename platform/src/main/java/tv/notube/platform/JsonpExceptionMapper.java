package tv.notube.platform;

import javax.ws.rs.ext.Provider;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Provider
public class JsonpExceptionMapper extends BaseExceptionMapper<JsonpRuntimeException> {

    public javax.ws.rs.core.Response toResponse(JsonpRuntimeException re) {
        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
            .entity( new JsonpPlatformResponse(JsonpPlatformResponse.Status.NOK,
                    getErrorMessage(re), null) )
            .build();
    }

}