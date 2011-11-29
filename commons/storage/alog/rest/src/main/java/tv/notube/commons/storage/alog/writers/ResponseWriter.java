package tv.notube.commons.storage.alog.writers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import tv.notube.commons.storage.alog.Response;
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

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ResponseWriter implements MessageBodyWriter<Response> {

    public boolean isWriteable(
            Class<?> aClass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType
    ) {
        return Response.class.isAssignableFrom(aClass);
    }

    public long getSize(
            Response response,
            Class<?> aClass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType
    ) {
        return -1;
    }

    public void writeTo(
            Response response,
            Class<?> aClass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, Object> stringObjectMultivaluedMap,
            OutputStream outputStream
    ) throws IOException, WebApplicationException {
        GsonBuilder builder = new GsonBuilder();
        DateTimeAdapter dta = new DateTimeAdapter();
        builder.registerTypeAdapter(DateTime.class, dta);
        Gson gson = builder.create();
        String json = gson.toJson(response);
        BufferedOutputStream baos = new BufferedOutputStream(outputStream);
        baos.write(json.getBytes());
        baos.close();
    }
}