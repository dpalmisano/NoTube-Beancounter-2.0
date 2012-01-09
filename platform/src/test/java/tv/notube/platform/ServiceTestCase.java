package tv.notube.platform;

import com.sun.jersey.api.json.JSONWithPadding;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ServiceTestCase {

    public JSONWithPadding fakeMethod1(
            @PathParam("username")  String username,
            @QueryParam("apikey")   String apiKey,
            @QueryParam("callback") String callback
    ) {
        throw new UnsupportedOperationException("will be never implemented");
    }

    @GET
    @Path("/analysis/{name}/{user}/{method}")
    public JSONWithPadding fakeMethod2(
            @PathParam("name") String name,
            @PathParam("user") String user,
            @PathParam("method") String methodName,
            @QueryParam("apikey") String apiKey,
            @QueryParam("callback") String callback,
            @Context UriInfo uriInfo
    ) {
        throw new UnsupportedOperationException("will be never implemented");
    }

    @Test
    public void testNullParameter() {
        String[] params = { "test-username", "test-apikey", null };
        try {
            Service.check(
                    this.getClass(),
                    "fakeMethod1",
                    params
            );
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
    }

    @Test
    public void testVariousAnnotations() {
        UriInfo uriInfo = null;
        Object[] params = { "test-name", "test-user", "test-method-name",
                "test-apikey", "test-callback", uriInfo };
        try {
            Service.check(
                    this.getClass(),
                    "fakeMethod2",
                    params
            );
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
    }

    @Test
    public void testUrl() throws UnsupportedEncodingException, MalformedURLException {
        URL finalRedirectUrl;
            finalRedirectUrl = new URL(
                    "http://" + URLDecoder.decode("demos.polymedia.it%2FNotube_Portal%2Fprofile.jsp%3Fid%3D5071%26session%3DjbPVkK40xX9S0htTsfXdi878N%26return%3Dsocialsubscription", "UTF-8")
            );
        System.out.print(finalRedirectUrl);
    }

}
