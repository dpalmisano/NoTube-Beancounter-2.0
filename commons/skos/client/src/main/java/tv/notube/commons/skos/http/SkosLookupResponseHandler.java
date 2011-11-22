package tv.notube.commons.skos.http;

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
public class SkosLookupResponseHandler
        implements ResponseHandler<SkosLookupResponse> {

    private Gson gson;

    public SkosLookupResponseHandler() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(SkosLookupResponse.class, new SkosLookupResponseAdapter());
        gson = builder.create();
    }

    public SkosLookupResponse handleResponse(HttpResponse httpResponse)
            throws ClientProtocolException, IOException {
         if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            final String errMsg = "Unexpected reply from SKOS: HTTP CODE: '"
                    + httpResponse.getStatusLine().getStatusCode() + "'";
            throw new ClientProtocolException(errMsg);
        }
        InputStream is = httpResponse.getEntity().getContent();
        BufferedInputStream bis = new BufferedInputStream(is);
        InputStreamReader reader = new InputStreamReader(bis);
        try {
            return gson.fromJson(reader, SkosLookupResponse.class);
        } finally {
            is.close();
            bis.close();
            reader.close();
        }
    }
}
