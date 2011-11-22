package tv.notube.commons.skos.writers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ResponseWriter implements MessageBodyWriter<List<URI>> {

    public boolean isWriteable(
            Class<?> aClass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType
    ) {
        return List.class.isAssignableFrom(aClass);
    }

    public long getSize(
            List<URI> response,
            Class<?> aClass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType
    ) {
        return -1;
    }

    public void writeTo(
            List<URI> response,
            Class<?> aClass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, Object> stringObjectMultivaluedMap,
            OutputStream outputStream
    ) throws IOException, WebApplicationException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(response);
        BufferedOutputStream baos = new BufferedOutputStream(outputStream);
        baos.write(json.getBytes());
        baos.close();
    }
}