package tv.notube.crawler.requester.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import tv.notube.commons.model.OAuthAuth;
import tv.notube.commons.model.SimpleAuth;
import tv.notube.crawler.requester.DefaultRequest;
import tv.notube.crawler.requester.RequestException;
import tv.notube.crawler.requester.ServiceResponse;
import tv.notube.crawler.requester.request.lastfm.LastFmRecentTracksResponse;
import tv.notube.crawler.requester.request.lastfm.LastFmRecentTracksResponseHandler;
import tv.notube.crawler.requester.request.lastfm.LastFmTrackServiceResponse;
import tv.notube.crawler.requester.request.twitter.TwitterResponse;
import tv.notube.crawler.requester.request.twitter.TwitterResponseAdapter;
import tv.notube.usermanager.services.auth.twitter.handlers.TwitterResponseHandler;

import java.io.IOException;
import java.io.InputStreamReader;
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

    private Gson gson;

    public TwitterRequest() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(
                TwitterResponse.class,
                new TwitterResponseAdapter()
        );
        gson = builder.create();
    }

    /**
     *
     * @return
     * @throws tv.notube.crawler.requester.RequestException
     */
    public ServiceResponse call() throws RequestException {
        String serviceEndpoint = service.getEndpoint().toString();
        OAuthAuth oaa = (OAuthAuth) auth;

        OAuthService twitterOAuth = new ServiceBuilder()
                .provider(TwitterApi.class)
                .apiKey(service.getApikey())
                .apiSecret(service.getSecret())
                .build();

        OAuthRequest request = new OAuthRequest(
                Verb.GET, serviceEndpoint);
        request.addQuerystringParameter("include_entities","true");
        twitterOAuth.signRequest(
                new Token(oaa.getSession(), oaa.getSecret()),
                request
        );
        Response response = request.send();
        InputStreamReader reader = new InputStreamReader(response.getStream());
        try {
            return gson.fromJson(reader, TwitterResponse.class);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RequestException("Error while closing reader", e);
            }
        }
    }

}
