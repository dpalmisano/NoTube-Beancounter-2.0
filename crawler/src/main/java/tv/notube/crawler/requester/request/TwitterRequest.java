package tv.notube.crawler.requester.request;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
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
 * <a href="http://twitter.com>twitter.com</a> specific implementation of
 * {@link tv.notube.crawler.requester.DefaultRequest}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TwitterRequest extends DefaultRequest {

    /**
     *
     * @return
     * @throws tv.notube.crawler.requester.RequestException
     */
    public ServiceResponse call() throws RequestException {
        throw new UnsupportedOperationException("NIY");
    }

}
