package tv.notube.commons.skos.http;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

public class SkosLookupResponseAdapter
        implements JsonDeserializer<SkosLookupResponse>, JsonSerializer<SkosLookupResponse> {

    public SkosLookupResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray array = jsonElement.getAsJsonArray();
        SkosLookupResponse result = new SkosLookupResponse();
        for(JsonElement item : array) {
            try {
                result.addUri(new URI(item.getAsString()));
            } catch (URISyntaxException e) {
                throw new JsonParseException("URL is not well formed", e);
            }
        }
        return result;
    }

    public JsonElement serialize(SkosLookupResponse lupediaResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        throw new UnsupportedOperationException("NIY");
    }
}
