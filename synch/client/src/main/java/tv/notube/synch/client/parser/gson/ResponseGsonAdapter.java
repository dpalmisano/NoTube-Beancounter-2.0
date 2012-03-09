package tv.notube.synch.client.parser.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.joda.time.DateTime;
import tv.notube.synch.model.Locked;
import tv.notube.synch.model.Released;
import tv.notube.synch.model.Status;

import java.lang.reflect.Type;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ResponseGsonAdapter implements JsonDeserializer<Status> {

    @Override
    public Status deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        long when = jsonElement.getAsJsonObject().get("when").getAsLong();
        String who = jsonElement.getAsJsonObject().get("who").getAsString();
        String status = jsonElement.getAsJsonObject().get("status").getAsString();

        if(status.equals("released")) {
            return new Released(who, new DateTime(when));
        }
        if(status.equals("locked")) {
            return new Locked(who, new DateTime(when));
        }
        throw new JsonParseException("response is not well formed");
    }
}
