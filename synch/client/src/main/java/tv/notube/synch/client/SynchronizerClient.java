package tv.notube.synch.client;

import tv.notube.synch.client.parser.SynchronizerParser;
import tv.notube.synch.client.parser.SynchronizerParserException;
import tv.notube.synch.model.Status;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SynchronizerClient {

    private URL base;

    private SynchronizerParser parser;

    public SynchronizerClient(URL base) {
        this.base = base;
        this.parser = new SynchronizerParser();
    }

    public SynchronizerClient(String base) {
        try {
            this.base = new URL(base);
        } catch (MalformedURLException e) {
            throw new RuntimeException("[" + base + "] is not well formed");
        }
        this.parser = new SynchronizerParser();
    }

    private URL buildQuery(String path) {
        try {
            return new URL(base.toString() + path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "[" + base.toString() + path + "] is not well-formed",
                    e
            );
        }
    }

    public Status getStatus() throws SynchronizerClientException {
        URL query = buildQuery("/status");
        InputStream is = call(query);
        try {
            return this.parser.parseStatus(is);
        } catch (SynchronizerParserException e) {
            final String errMsg = "error while parsing response";
            throw new SynchronizerClientException(errMsg, e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                final String errMsg = "error while closing response stream";
                throw new SynchronizerClientException(errMsg, e);
            }
        }
    }

    private InputStream call(URL query) throws SynchronizerClientException {
        URLConnection urlConnection;
        try {
            urlConnection = query.openConnection();
        } catch (IOException e) {
            final String errMsg = "Error while opening connection on [" + query + "]";
            throw new SynchronizerClientException(errMsg, e);
        }
        try {
            urlConnection.connect();
        } catch (IOException e) {
            final String errMsg = "Error while connecting on [" + query + "]";
            throw new SynchronizerClientException(errMsg, e);
        }
        InputStream is;
        try {
            is = urlConnection.getInputStream();
        } catch (IOException e) {
            final String errMsg = "Error while opening response stream from [" + query + "]";
            throw new SynchronizerClientException(errMsg, e);
        }
        return is;
    }

    public UUID getToken(String processName) throws SynchronizerClientException {
        URL query = buildQuery("/token/" + processName);
        InputStream is = call(query);
        try {
            return this.parser.parseToken(is);
        } catch (SynchronizerParserException e) {
            final String errMsg = "error while parsing response";
            throw new SynchronizerClientException(errMsg, e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                final String errMsg = "error while closing response stream";
                throw new SynchronizerClientException(errMsg, e);
            }
        }
    }

    public Response lock(String processName, UUID token)
            throws SynchronizerClientException {
        URL query = buildQuery("/lock/" + processName + "/" + token.toString());
        InputStream is = call(query);
        try {
            return this.parser.parseResponse(is);
        } catch (SynchronizerParserException e) {
            final String errMsg = "error while parsing response";
            throw new SynchronizerClientException(errMsg, e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                final String errMsg = "error while closing response stream";
                throw new SynchronizerClientException(errMsg, e);
            }
        }
    }

    public Response release(String processName, UUID token) throws
            SynchronizerClientException {
        URL query = buildQuery("/release/" + processName + "/" + token.toString
                ());
        InputStream is = call(query);
        try {
            return this.parser.parseResponse(is);
        } catch (SynchronizerParserException e) {
            final String errMsg = "error while parsing response";
            throw new SynchronizerClientException(errMsg, e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                final String errMsg = "error while closing response stream";
                throw new SynchronizerClientException(errMsg, e);
            }
        }
    }

}
