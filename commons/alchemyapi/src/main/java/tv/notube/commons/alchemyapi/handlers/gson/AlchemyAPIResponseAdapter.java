package tv.notube.commons.alchemyapi.handlers.gson;

import com.google.gson.*;
import tv.notube.commons.alchemyapi.AlchemyAPIResponse;
import tv.notube.commons.alchemyapi.Concept;
import tv.notube.commons.alchemyapi.NamedEntity;
import tv.notube.commons.alchemyapi.handlers.AlchemyAPIResponseHandler;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * {@link JsonDeserializer} implementation to deserialize
 * {@link AlchemyAPIResponse} from <i>JSON</i>
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AlchemyAPIResponseAdapter implements
        JsonDeserializer<AlchemyAPIResponse> {

    private AlchemyAPIResponseHandler.Type type;

    public AlchemyAPIResponseAdapter(AlchemyAPIResponseHandler.Type type) {
        this.type = type;
    }

    public AlchemyAPIResponse deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        if (this.type.equals(AlchemyAPIResponseHandler.Type.CONCEPTS)) {
            return getConceptsResponse(jsonElement);
        }
        if (this.type.equals(AlchemyAPIResponseHandler.Type.ENTITIES)) {
            return getEntitiesResponse(jsonElement);
        }
        throw new IllegalArgumentException("Type '" + this.type + "' is not " +
                "supported");
    }

    private AlchemyAPIResponse getEntitiesResponse(JsonElement jsonElement) {
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        String status = jsonResponse.get("status").getAsString();
        AlchemyAPIResponse response = new AlchemyAPIResponse(status);
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

    private AlchemyAPIResponse getConceptsResponse(
            JsonElement jsonElement
    ) {
        JsonObject jsonResponse = jsonElement.getAsJsonObject();
        String status = jsonResponse.get("status").getAsString();
        AlchemyAPIResponse response = new AlchemyAPIResponse(status);
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
