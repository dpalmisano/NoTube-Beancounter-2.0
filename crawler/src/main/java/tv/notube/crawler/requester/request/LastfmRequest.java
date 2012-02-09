package tv.notube.crawler.requester.request;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import tv.notube.commons.model.auth.SimpleAuth;
import tv.notube.crawler.requester.DefaultRequest;
import tv.notube.crawler.requester.RequestException;
import tv.notube.crawler.requester.ServiceResponse;
import tv.notube.crawler.requester.request.lastfm.LastFmRecentTracksResponse;
import tv.notube.crawler.requester.request.lastfm.LastFmRecentTracksResponseHandler;
import tv.notube.crawler.requester.request.lastfm.LastFmTrackServiceResponse;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * <a href="http://last.fm>last.fm</a> specific implementation of
 * {@link DefaultRequest}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LastfmRequest extends DefaultRequest {

    /**
     *
     * @return
     * @throws RequestException
     */
    public ServiceResponse call() throws RequestException {
        URL serviceEndpoint = service.getEndpoint();
        SimpleAuth sa = (SimpleAuth) auth;
        Map<String, String> params = new HashMap<String, String>();
        params.put("method", "user.getrecenttracks");
        params.put("user", sa.getUsername());
        params.put("limit", "100");
        params.put("format", "json");

        String queryString = buildQueryString(serviceEndpoint, params);
        queryString +=  "api_key=" + service.getApikey();

        HttpGet method = new HttpGet(queryString);
        LastFmRecentTracksResponse response;

        ResponseHandler<LastFmRecentTracksResponse> lrh =
                new LastFmRecentTracksResponseHandler();

        try {
            response = httpClient.execute(method, lrh);
        } catch (IOException e) {
            throw new RequestException("", e);
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
        }
        return new LastFmTrackServiceResponse(response.getTracks());
    }

    private String buildQueryString(URL serviceEndpoint, Map<String, String> params) {
        String ep = serviceEndpoint.toString();
        for (String name : params.keySet()) {
            ep += name + "=" + params.get(name) + "&";
        }
        return ep;
    }
}
