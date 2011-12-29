package tv.notube.platform;

import javax.ws.rs.core.Response;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class Service {

    protected String _corsHeaders;

    protected Response makeCORS(
            Response.ResponseBuilder req,
            String returnMethod
    ) {
        Response.ResponseBuilder rb = req
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

        if (!"".equals(returnMethod)) {
            rb.header("Access-Control-Allow-Headers", returnMethod);
        }
        return rb.build();
    }

    protected Response makeCORS(Response.ResponseBuilder req) {
        return makeCORS(req, _corsHeaders);
    }

}
