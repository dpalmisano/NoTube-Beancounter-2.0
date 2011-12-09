package tv.notube.crawler.requester.request.facebook;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class FacebookResponseAdapter implements
        JsonDeserializer<FacebookResponse> {

    public FacebookResponse deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        System.out.println(jsonElement);
        throw new UnsupportedOperationException("NIY");
    }
}
