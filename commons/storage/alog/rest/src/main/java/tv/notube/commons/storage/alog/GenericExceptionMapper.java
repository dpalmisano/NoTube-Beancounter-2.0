package tv.notube.commons.storage.alog;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper extends BaseExceptionMapper<RuntimeException> {

    public javax.ws.rs.core.Response toResponse(RuntimeException re) {
        return javax.ws.rs.core.Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(getErrorMessage(re))
            .build();
    }

}