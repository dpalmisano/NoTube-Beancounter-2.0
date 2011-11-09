package tv.notube.crawler.requester.request.lastfm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LastFmRecentTracksResponseHandler
        implements ResponseHandler<LastFmRecentTracksResponse> {

    private Gson gson;

    public LastFmRecentTracksResponseHandler() {
        GsonBuilder builder = new GsonBuilder();
        LastFmRecentTracksResponseAdapter adapter = new LastFmRecentTracksResponseAdapter();
        builder.registerTypeAdapter(LastFmRecentTracksResponse.class, adapter);
        gson = builder.create();
    }

    @Override
    public LastFmRecentTracksResponse handleResponse(HttpResponse httpResponse)
            throws ClientProtocolException, IOException {
        if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            final String errMsg = "Unexpected reply from Lastfm: HTTP CODE: '"
                    + httpResponse.getStatusLine().getStatusCode() + "'";
            throw new ClientProtocolException(errMsg);
        }
        InputStream is = httpResponse.getEntity().getContent();
        BufferedInputStream bis = new BufferedInputStream(is);
        InputStreamReader reader = new InputStreamReader(bis);
        try {
            return gson.fromJson(reader, LastFmRecentTracksResponse.class);
        } finally {
            is.close();
            bis.close();
            reader.close();
        }
    }
}
