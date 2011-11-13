package tv.notube.commons.alchemyapi.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import tv.notube.commons.alchemyapi.AlchemyAPIResponse;
import tv.notube.commons.alchemyapi.handlers.gson.AlchemyAPIResponseAdapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * {@link ResponseHandler} implementation to handle <i>HTTP</i> responses
 * and wrap them with {@link AlchemyAPIResponse}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AlchemyAPIResponseHandler implements ResponseHandler<AlchemyAPIResponse> {

    public enum Type {
        CONCEPTS,
        ENTITIES
    }

    private Gson gson;

    public AlchemyAPIResponseHandler(Type type) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(
                AlchemyAPIResponse.class,
                new AlchemyAPIResponseAdapter(type)
        );
        gson = gsonBuilder.create();
    }

    public AlchemyAPIResponse handleResponse(HttpResponse httpResponse)
            throws ClientProtocolException, IOException {
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status != HttpStatus.SC_OK) {
            return new AlchemyAPIResponse(AlchemyAPIResponse.Status.ERROR);
        }
        InputStream inputStream = httpResponse.getEntity().getContent();
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        InputStreamReader reader = new InputStreamReader(bis);
        try {
            return gson.fromJson(reader, AlchemyAPIResponse.class);
        } finally {
            reader.close();
            bis.close();
            inputStream.close();
        }
    }
}
