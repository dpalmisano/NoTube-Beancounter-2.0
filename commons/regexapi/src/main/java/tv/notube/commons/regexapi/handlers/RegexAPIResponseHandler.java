package tv.notube.commons.regexapi.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import tv.notube.commons.regexapi.RegexAPIResponse;
import tv.notube.commons.regexapi.handlers.gson.RegexAPIResponseAdapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * {@link ResponseHandler} implementation to handle <i>HTTP</i> responses
 * and wrap them with {@link RegexAPIResponse}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RegexAPIResponseHandler implements ResponseHandler<RegexAPIResponse> {

    public enum Type {
        CONCEPTS,
        ENTITIES
    }

    private Gson gson;

    public RegexAPIResponseHandler(Type type) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(
                RegexAPIResponse.class,
                new RegexAPIResponseAdapter(type)
        );
        gson = gsonBuilder.create();
    }

    public RegexAPIResponse handleResponse(HttpResponse httpResponse)
            throws ClientProtocolException, IOException {
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status != HttpStatus.SC_OK) {
            return new RegexAPIResponse(RegexAPIResponse.Status.ERROR);
        }
        InputStream inputStream = httpResponse.getEntity().getContent();
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        InputStreamReader reader = new InputStreamReader(bis);
        try {
            return gson.fromJson(reader, RegexAPIResponse.class);
        } finally {
            reader.close();
            bis.close();
            inputStream.close();
        }
    }
}
