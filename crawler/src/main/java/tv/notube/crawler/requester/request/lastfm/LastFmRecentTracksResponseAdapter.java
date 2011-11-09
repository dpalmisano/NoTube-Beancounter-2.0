package tv.notube.crawler.requester.request.lastfm;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LastFmRecentTracksResponseAdapter
        implements JsonDeserializer<LastFmRecentTracksResponse>, JsonSerializer<LastFmRecentTracksResponse> {

    private DateTimeFormatter dateTimeFormatter;

    public LastFmRecentTracksResponseAdapter() {
        dateTimeFormatter = DateTimeFormat.forPattern("dd MMM yyyy, HH:mm");
    }

    public LastFmRecentTracksResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        LastFmRecentTracksResponse result = new LastFmRecentTracksResponse();
        JsonArray tracks = jsonElement
                .getAsJsonObject()
                .getAsJsonObject("recenttracks")
                .getAsJsonArray("track");
        for(JsonElement track : tracks) {
            LastFmTrack lastFmTrack = new LastFmTrack();
            JsonObject trackObj = track.getAsJsonObject();
            String artistName = trackObj.getAsJsonObject("artist").get("#text").getAsString();
            String artistMbid = trackObj.getAsJsonObject("artist").get("mbid").getAsString();
            LastFmArtist artist = new LastFmArtist(artistName, artistMbid);
            lastFmTrack.setArtist(artist);

            String trackName = trackObj.get("name").getAsString();
            lastFmTrack.setName(trackName);
            String trackUrl = trackObj.get("url").getAsString();
            try {
                lastFmTrack.setUrl(new URL(trackUrl));
            } catch (MalformedURLException e) {
                throw new JsonParseException("Error, URL '" + trackUrl + "' is not a valid URL", e);
            }

            String trackDate;
            if(trackObj.getAsJsonObject("date") != null) {
                trackDate = trackObj.getAsJsonObject("date").get("#text").getAsString();
                dateTimeFormatter.parseDateTime(trackDate.trim());
                lastFmTrack.setDate(dateTimeFormatter.parseDateTime(trackDate.trim()));
            }
            result.addTrack(lastFmTrack);
        }
        return result;
    }

    public JsonElement serialize(LastFmRecentTracksResponse lastFmRecentTracksResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        throw new UnsupportedOperationException("NIY");
    }
}