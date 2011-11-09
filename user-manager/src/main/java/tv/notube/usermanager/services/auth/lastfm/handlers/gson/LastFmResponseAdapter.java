package tv.notube.usermanager.services.auth.lastfm.handlers.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import tv.notube.usermanager.services.auth.lastfm.LastFmResponse;

import java.lang.reflect.Type;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LastFmResponseAdapter
        implements JsonDeserializer<LastFmResponse>, JsonSerializer<LastFmResponse> {
    public LastFmResponse deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        JsonObject sessionObj = obj.getAsJsonObject("session");
        String name = sessionObj.get("name").getAsString();
        String key = sessionObj.get("key").getAsString();
        return new LastFmResponse(name, key);
    }

    /**
     *
     * {"session":{"name":"davidepalmisano","key":"af65659c785315b90b54eea682e66433","subscriber":"0"}}
     *
     * @param lastFmResponse
     * @param type
     * @param jsonSerializationContext
     * @return
     */

    public JsonElement serialize(
            LastFmResponse lastFmResponse,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        throw new UnsupportedOperationException("NIY");
    }
}
