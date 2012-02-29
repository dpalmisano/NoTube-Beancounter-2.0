package tv.notube.commons.regexapi.handlers.gson;

import com.google.gson.*;
import tv.notube.commons.regexapi.RegexAPIResponse;
import tv.notube.commons.regexapi.Concept;
import tv.notube.commons.regexapi.NamedEntity;
import tv.notube.commons.regexapi.handlers.RegexAPIResponseHandler;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * {@link JsonDeserializer} implementation to deserialize
 * {@link RegexAPIResponse} from <i>JSON</i>
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RegexAPIResponseAdapter implements
        JsonDeserializer<RegexAPIResponse> {

    private RegexAPIResponseHandler.Type type;

    public RegexAPIResponseAdapter(RegexAPIResponseHandler.Type type) {
        this.type = type;
    }

    public RegexAPIResponse deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        if (this.type.equals(RegexAPIResponseHandler.Type.CONCEPTS)) {
            return getConceptsResponse(jsonElement);
        }
        if (this.type.equals(RegexAPIResponseHandler.Type.ENTITIES)) {
            return getEntitiesResponse(jsonElement);
        }
        throw new IllegalArgumentException("Type '" + this.type + "' is not " +
                "supported");
    }

    private RegexAPIResponse getEntitiesResponse(JsonElement jsonElement) {
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        String status = jsonResponse.get("status").getAsString();
        RegexAPIResponse response = new RegexAPIResponse(status);
        JsonArray jsonEntities = jsonResponse.getAsJsonArray("entities");
        for (JsonElement jsonEntity : jsonEntities) {
            float relevance = jsonEntity.getAsJsonObject().get("relevance")
                    .getAsFloat();
            String type = jsonEntity.getAsJsonObject().get("type")
                    .getAsString();
            if (jsonEntity.getAsJsonObject().get("disambiguated") != null) {
                String dbpedia = jsonEntity.getAsJsonObject().get
                        ("disambiguated").getAsJsonObject().get("dbpedia").getAsString();
                NamedEntity namedEntity;
                try {
                    namedEntity = new NamedEntity(
                            new URI(dbpedia),
                            relevance
                    );
                    namedEntity.setType(type);
                    response.addIdentified(namedEntity);
                } catch (URISyntaxException e) {
                    // malformed URI from dbpedia - it happens, just skip
                }
            }
        }

        return response;
    }

    private RegexAPIResponse getConceptsResponse(
            JsonElement jsonElement
    ) {
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        String status = jsonResponse.get("status").getAsString();
        RegexAPIResponse response = new RegexAPIResponse(status);
        JsonArray jsonConcepts = jsonResponse.getAsJsonArray("concepts");
        for (JsonElement jsonConcept : jsonConcepts) {
            float relevance = jsonConcept.getAsJsonObject().get("relevance")
                    .getAsFloat();
            String dbpedia = jsonConcept.getAsJsonObject().get("dbpedia")
                    .getAsString();
            try {
                response.addIdentified(new Concept(new URI(dbpedia), relevance));
            } catch (URISyntaxException e) {
                // malformed URI from dbpedia - it happens, just skip
            }
        }
        return response;
    }

}
