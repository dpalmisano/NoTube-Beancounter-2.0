package tv.notube.usermanager.services.auth.twitter.handlers;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import tv.notube.usermanager.services.auth.twitter.TwitterOAuthResponse;

import java.io.*;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TwitterResponseHandler implements ResponseHandler<TwitterOAuthResponse> {

    public TwitterOAuthResponse handleResponse(HttpResponse httpResponse)
            throws ClientProtocolException, IOException {
        if(!httpResponse.getStatusLine().equals(HttpStatus.SC_OK)) {
            return new TwitterOAuthResponse(TwitterOAuthResponse.Status.NOK);
        }
        InputStream is = httpResponse.getEntity().getContent();
        BufferedInputStream bis = new BufferedInputStream(is);
        InputStreamReader reader = new InputStreamReader(bis);
        BufferedReader br = new BufferedReader(reader);
        throw new UnsupportedOperationException("niy");
    }
}
