package tv.notube.usermanager.services.auth.oauth;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class OAuthUtils {

    public enum METHOD {
        GET,
        POST,
        DELETE,
        PUT
    }

    public static String getStringSignature(
            METHOD method,
            URL url,
            Map<String, String> parameters
    ) {
        Set<String> keySet = parameters.keySet();
        List<String> parameterNames = new ArrayList<String>(keySet);
        Collections.sort(parameterNames);
        String s = "";
        for(String parameterName : parameterNames) {
            s += parameterName + '=' + parameters.get(parameterName) + '&';
        }
        try {
            s = URLEncoder.encode(s.substring(0, s.length() - 1), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error while encoding parameters", e);
        }
        try {
            return method.name() + '&' + URLEncoder.encode(url.toString()
                    .toLowerCase(), "UTF-8") + '&' + s;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error while encoding parameters", e);
        }
    }

    public static List<NameValuePair> getHttpNameValuePairs(
            Map<String, String> parameters, String signature) {

        List<NameValuePair> result = new ArrayList<NameValuePair>();
        for(String key : parameters.keySet()) {
            result.add(new BasicNameValuePair(key, parameters.get(key)));
        }
        result.add(new BasicNameValuePair("oauth_signature", signature));
        return result;
    }

    public static Header getSingleHeader(
             Map<String, String> parameters,
             String signature
    ) {
        final String AUTH = "Authorization";
        final String OAUTH = "OAuth";
        final String PARAM_PATTERN = "%s=\"%s\",";
        String headerValue = OAUTH;
        for(String key : parameters.keySet()) {
            try {
                headerValue += " " + String.format(
                        PARAM_PATTERN,
                        key,
                        URLEncoder.encode(parameters.get(key), "UTF-8")
                );
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("", e);
            }
        }
        headerValue += " " + String.format(
                PARAM_PATTERN,
                "oauth_signature",
                signature);
        Header header = new BasicHeader(
                AUTH,
                headerValue.substring(0, headerValue.length())
        );
        return header;
    }

    public static Header[] getHttpHeader(
            Map<String, String> parameters, String signature) {

        Header[] header = new Header[parameters.size() + 1];
        int i = 0;
        for(String key : parameters.keySet()) {
            header[i] = new BasicHeader(key, parameters.get(key));
            i++;
        }
        header[i] = new BasicHeader("oauth_signature", signature);
        return header;
    }

    public static String getHashedSignature(
            METHOD method,
            URL url,
            Map<String, String> parameters,
            String consumerSecret,
            String tokenSecret
    ) {
        String signature = getStringSignature(method, url, parameters);
        String ets;
        String ecs;
        try {
            ecs = URLEncoder.encode(consumerSecret, "UTF-8");
            ets = URLEncoder.encode(tokenSecret, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("", e);
        }
        String keyString = ecs + '&' + ets;
        SecretKey key = new SecretKeySpec(keyString.getBytes(), "HmacSHA1");
        Mac m;
        try {
            m = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("", e);
        }
        try {
            m.init(key);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("", e);
        }
        byte[] mac;
        try {
            mac = m.doFinal(signature.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("");
        }
        return new BASE64Encoder().encode(mac);

    }

    public static String getNonce() {
        return String.valueOf(System.currentTimeMillis());
    }

}
