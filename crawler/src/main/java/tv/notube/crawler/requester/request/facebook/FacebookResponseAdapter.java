package tv.notube.crawler.requester.request.facebook;

import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class FacebookResponseAdapter implements
        JsonDeserializer<FacebookResponse> {

    private DateTimeFormatter dateTimeFormatter;

    public FacebookResponseAdapter() {
        dateTimeFormatter = DateTimeFormat.forPattern
                ("YYYY-MM-dd'T'HH:mm:ssZ");
    }

    public FacebookResponse deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        JsonArray jsonData = jsonResponse.get("data").getAsJsonArray();

        List<FacebookLike> likes = new ArrayList<FacebookLike>();
        for(JsonElement jsonLike : jsonData) {
            String name = jsonLike.getAsJsonObject().get("name").getAsString();
            String category = jsonLike.getAsJsonObject().get("category").getAsString();
            long id = jsonLike.getAsJsonObject().get("id").getAsLong();
            DateTime createdTime = dateTimeFormatter.parseDateTime(
                    jsonLike.getAsJsonObject().get("created_time").getAsString()
            );
            likes.add(new FacebookLike(name, category, id, createdTime));
        }
        return new FacebookResponse(likes);
    }
}
