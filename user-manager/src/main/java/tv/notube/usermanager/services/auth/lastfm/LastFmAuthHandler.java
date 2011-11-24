package tv.notube.usermanager.services.auth.lastfm;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import tv.notube.commons.model.SimpleAuth;
import tv.notube.commons.model.Service;
import tv.notube.commons.model.User;
import tv.notube.usermanager.services.auth.AuthHandlerException;
import tv.notube.usermanager.services.auth.DefaultAuthHandler;
import tv.notube.usermanager.services.auth.lastfm.handlers.LastFmResponseHandler;
import tv.notube.usermanager.services.auth.oauth.OAuthToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class LastFmAuthHandler extends DefaultAuthHandler {

    private static Logger logger = Logger.getLogger(LastFmAuthHandler.class);

    private HttpClient httpClient = new DefaultHttpClient();

    public LastFmAuthHandler(Service service) {
        super(service);
    }

    public User auth(User user, String token) throws AuthHandlerException {
        String authEndpoint = service.getSessionEndpoint();
        String apikey = service.getApikey();
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", apikey);
        params.put("method", "auth.getSession");
        params.put("token", token);
        String api_sig = getApiSig(params, service.getSecret());
        params.put("format", "json");
        for (String name : asSortedList(params.keySet())) {
            authEndpoint += name + "=" + params.get(name) + "&";
        }
        authEndpoint += "api_sig=" + api_sig;
        logger.info("auth endpoint=" + authEndpoint);

        HttpGet method = new HttpGet(authEndpoint);
        LastFmResponse response;
        ResponseHandler<LastFmResponse> lrh = new LastFmResponseHandler();
        try {
            response = httpClient.execute(method, lrh);
        } catch (IOException e) {
            throw new AuthHandlerException("", e);
        }
        String session = response.getKey();
        String username = response.getName();
        user.addService(service.getName(), new SimpleAuth(session, username));
        try {
            return user;
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
        }
    }

    public User auth(User user, String token, String verifier) throws AuthHandlerException {
        if (verifier == null) {
            return auth(user, token);
        } else {
            throw new AuthHandlerException(
                    "This step of the OAuth protocol is not required by LastFM"
            );
        }
    }

    public OAuthToken getToken(String username) throws AuthHandlerException {
        throw new AuthHandlerException(
                "This step of the OAuth protocol is not required by LastFM"
        );
    }

    private String getApiSig(Map<String, String> params, String secret) {
        String sig = "";
        for (String name : asSortedList(params.keySet())) {
            sig += name + params.get(name);
        }
        sig += secret;
        return md5(sig);
    }

    private String md5(String string) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
        algorithm.reset();
        try {
            algorithm.update(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is not available", e);
        }
        byte messageDigest[] = algorithm.digest();
        BigInteger bigInt = new BigInteger(1, messageDigest);
        String hashtext = bigInt.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext.toString();
    }

    private <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list);
        return list;
    }
}
