package tv.notube.extension.profilingline.lupedia;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import tv.notube.extension.profilingline.lupedia.http.LupediaResponseHandler;
import tv.notube.extension.profilingline.lupedia.http.json.LupediaResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultLupediaImpl implements Lupedia {

    private final static String LUPEDIA = "http://lupedia.ontotext.com/lookup/text2json";

    private HttpClient httpClient;

    public DefaultLupediaImpl() {
        httpClient = new DefaultHttpClient();
    }

    public List<URI> getResources(String text) throws LupediaException {
        HttpPost method = new HttpPost(LUPEDIA);
        List <NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("lookupText", text));
        nvps.add(new BasicNameValuePair("skip_ldata", "true"));
        try {
            method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 seems to be not supported");
        }
        LupediaResponse response;
        ResponseHandler<LupediaResponse> lrh = new LupediaResponseHandler();
        try {
            response = httpClient.execute(method, lrh);
        } catch (IOException e) {
            throw new LupediaException("Error while calling Lupedia with text: '" + text + "'", e);
        }
        try {
        return response.getEntriesAsURIs();
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
        }
    }
}
