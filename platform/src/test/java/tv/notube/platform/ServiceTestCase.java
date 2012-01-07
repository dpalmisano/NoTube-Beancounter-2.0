package tv.notube.platform;

import com.sun.jersey.api.json.JSONWithPadding;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ServiceTestCase {

    public JSONWithPadding fakeMethod(
            @PathParam("username")  String username,
            @QueryParam("apikey")   String apiKey,
            @QueryParam("callback") String callback
    ) {
        throw new UnsupportedOperationException("will be never implemented");
    }

    @Test
    public void testNullParameter() {
        String[] params = { "test-username", "test-apikey", null };
        try {
            Service.check(
                    this.getClass(),
                    "fakeMethod",
                    params
            );
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
    }

}
