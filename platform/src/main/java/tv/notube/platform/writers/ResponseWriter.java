package tv.notube.platform.writers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import tv.notube.commons.model.gson.DateTimeAdapter;
import tv.notube.platform.Response;
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
import java.util.ArrayList;
import java.util.List;

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
        setCORSParameters(stringObjectMultivaluedMap);
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        builder.setPrettyPrinting();
        DateTimeAdapter dta = new DateTimeAdapter();
        builder.registerTypeAdapter(DateTime.class, dta);
        Gson gson = builder.create();
        String json = gson.toJson(response);
        BufferedOutputStream baos = new BufferedOutputStream(outputStream);
        baos.write(json.getBytes());
        baos.close();
    }

    private void setCORSParameters(MultivaluedMap<String, Object> header) {
        List<String> headers = new ArrayList<String>();
        headers.add("X-Requested-With");
        headers.add("Origin");
        header.add("Access-Control-Allow-Headers", headers);

        List<String> methods = new ArrayList<String>();
        methods.add("GET");
        methods.add("POST");
        methods.add("DELETE");
        methods.add("PUT");
        header.add("Access-Control-Allow-Methods", methods);

        List<String> origin = new ArrayList<String>();
        origin.add("*");
        header.add("Access-Control-Allow-Origin", origin);

        List<String> cache = new ArrayList<String>();
        cache.add("no-cache");
        cache.add("no-store");
        cache.add("max-age=0");
        cache.add("must-revalidate");
        header.add("Cache-Control", cache);
    }
}