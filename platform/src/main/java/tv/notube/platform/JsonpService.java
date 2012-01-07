package tv.notube.platform;

import com.sun.jersey.api.json.JSONWithPadding;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class JsonpService extends Service {

    protected JSONWithPadding error(
            Exception e,
            String message,
            String callback
    ) {
        return new JSONWithPadding(
                new JsonpPlatformResponse(
                        JsonpPlatformResponse.Status.NOK,
                        message,
                        e.getMessage()
                ),
                callback
        );
    }

}
