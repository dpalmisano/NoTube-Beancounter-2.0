package tv.notube.extension.profilingline.lupedia.http.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LupediaResponseAdapter
        implements JsonDeserializer<LupediaResponse>, JsonSerializer<LupediaResponse> {

    public LupediaResponse deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        LupediaResponse result = new LupediaResponse();
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            LupediaEntry entry = new LupediaEntry();
            entry.setInstanceUri(jsonObject.get("instanceUri").getAsString());
            entry.setWeight(jsonObject.get("weight").getAsFloat());
            result.addEntry(entry);
        }
        return result;
    }

    public JsonElement serialize(
            LupediaResponse lupediaResponse,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        throw new UnsupportedOperationException("NIY");
    }
}
