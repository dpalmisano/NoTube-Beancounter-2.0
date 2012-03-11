package tv.notube.synch.client.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tv.notube.synch.client.Response;
import tv.notube.synch.client.parser.gson.StatusGsonAdapter;
import tv.notube.synch.model.Status;

import java.io.*;
import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SynchronizerParser {

    private Gson gson;

    public SynchronizerParser() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Status.class, new StatusGsonAdapter());
        gson = builder.create();
    }

    public Status parseStatus(InputStream is) throws SynchronizerParserException {
        InputStreamReader isr = new InputStreamReader(is);
        return gson.fromJson(isr, Status.class);
    }

    public UUID parseToken(InputStream is) throws SynchronizerParserException {
        BufferedReader br
                = new BufferedReader(
                new InputStreamReader(is)
        );
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new SynchronizerParserException(
                    "Error while reading response",
                    e
            );
        }
        try {
            return UUID.fromString(sb.toString().replace("\"", ""));
        } catch (Exception e) {
            throw new SynchronizerParserException(
                    "[" + sb.toString() + "] is not a valid UUID",
                    e
            );
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                throw new SynchronizerParserException(
                        "Error while closing reader",
                        e
                );
            }
        }
    }

    public Response parseResponse(InputStream is)
            throws SynchronizerParserException {
        InputStreamReader isr = new InputStreamReader(is);
        return gson.fromJson(isr, Response.class);
    }
}
