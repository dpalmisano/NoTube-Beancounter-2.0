package tv.notube.platform;

import com.sun.jersey.api.json.JSONWithPadding;

import javax.ws.rs.core.Response;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class JsonService extends Service {

    protected Response error(
            Exception e,
            String message
    ) {
        Response.ResponseBuilder rb = Response.serverError();
        rb.entity(
                new PlatformResponse(
                        PlatformResponse.Status.NOK,
                        message,
                        e.getMessage()
                )
        );
        return rb.build();
    }

}
