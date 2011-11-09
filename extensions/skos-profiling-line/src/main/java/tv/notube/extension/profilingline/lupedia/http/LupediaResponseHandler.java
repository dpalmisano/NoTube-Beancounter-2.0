package tv.notube.extension.profilingline.lupedia.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import tv.notube.extension.profilingline.lupedia.http.json.LupediaResponse;
import tv.notube.extension.profilingline.lupedia.http.json.LupediaResponseAdapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LupediaResponseHandler
        implements ResponseHandler<LupediaResponse> {

    private Gson gson;

    public LupediaResponseHandler() {
        GsonBuilder builder = new GsonBuilder();
        LupediaResponseAdapter adapter = new LupediaResponseAdapter();
        builder.registerTypeAdapter(LupediaResponse.class, adapter);
        gson = builder.create();
    }

    public LupediaResponse handleResponse(HttpResponse httpResponse)
            throws ClientProtocolException, IOException {
        if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            final String errMsg = "Unexpected reply from Lupedia: HTTP CODE: '"
                    + httpResponse.getStatusLine().getStatusCode() + "'";
            throw new ClientProtocolException(errMsg);
        }
        InputStream is = httpResponse.getEntity().getContent();
        BufferedInputStream bis = new BufferedInputStream(is);
        InputStreamReader reader = new InputStreamReader(bis);
        try {
            return gson.fromJson(reader, LupediaResponse.class);
        } finally {
            is.close();
            bis.close();
            reader.close();
        }
    }
}
