package tv.notube.commons.alchemyapi;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import tv.notube.commons.alchemyapi.handlers.AlchemyAPIResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AlchemyAPI {

    private final static String CONCEPTS = "http://access.alchemyapi.com/calls/text/TextGetRankedConcepts?apikey=%s&outputMode=json";

    private final static String ENTITIES = "http://access.alchemyapi.com/calls/text/TextGetRankedNamedEntities?apikey=%s&outputMode=json";

    private HttpClient httpClient;

    private String apikey;

    public AlchemyAPI(String apikey) {
        httpClient = new DefaultHttpClient();
        this.apikey = apikey;
    }

    public AlchemyAPIResponse getRankedConcept(String text) throws
            AlchemyAPIException {
        if (text == null) {
            throw new IllegalArgumentException("Parameter text cannot be " +
                    "null");
        }
        HttpPost method = new HttpPost(String.format(CONCEPTS, apikey));
        ResponseHandler<AlchemyAPIResponse> aarh = new
                AlchemyAPIResponseHandler(AlchemyAPIResponseHandler.Type.CONCEPTS);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(
                "text",
                text)
        );
        try {
            method.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            final String errMsg = "Encoding not supported";
            throw new AlchemyAPIException(errMsg, e);
        }

        try {
            return httpClient.execute(method, aarh);
        } catch (IOException e) {
            final String errMsg = "Error while calling AlchemyAPI";
            throw new AlchemyAPIException(errMsg, e);
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
        }
    }

    public AlchemyAPIResponse getNamedEntities(String text) throws AlchemyAPIException {
         if (text == null) {
            throw new IllegalArgumentException("Parameter text cannot be " +
                    "null");
        }
        HttpPost method = new HttpPost(String.format(ENTITIES, apikey));
        ResponseHandler<AlchemyAPIResponse> aarh = new
                AlchemyAPIResponseHandler(AlchemyAPIResponseHandler.Type.ENTITIES);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair(
                "text",
                text)
        );
        try {
            method.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            final String errMsg = "Encoding not supported";
            throw new AlchemyAPIException(errMsg, e);
        }

        try {
            return httpClient.execute(method, aarh);
        } catch (IOException e) {
            final String errMsg = "Error while calling AlchemyAPI";
            throw new AlchemyAPIException(errMsg, e);
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
        }
    }

}
