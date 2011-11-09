package tv.notube.extension.profilingline.skos.http;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

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
                result.addSkos(new URI(item.getAsString()));
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
