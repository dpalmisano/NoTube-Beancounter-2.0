package tv.notube.platform;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class AnalyticsServiceTestCase extends AbstractJerseyTestCase {

    public AnalyticsServiceTestCase() {
        super(9995);
    }

    @Test
    public void getAnalysisResult() throws IOException {
        /**
         *
         *  @Path("/analysis/{name}/{user}/{method}/{param}")
    public Response getAnalysisResult(
            @PathParam("name") String name,
            @PathParam("user") String user,
            @PathParam("method") String methodName,
            @PathParam("param") String param
    )
         *
         *
         */
        final String baseQuery = "analytics/analysis/%s/%s/%s/%s";
        final String name = "timeframe-analysis";
        final String user = "8c33b0e6-d3cf-4909-b04c-df93056e64a8";
        final String methodName = "getStatistics";
        final String param = "5";

        final String query = String.format(
                baseQuery,
                name,
                user,
                methodName,
                param
        );

        // Perform GET
        GetMethod getMethod = new GetMethod(base_uri + query);
        HttpClient client = new HttpClient();
        int result = client.executeMethod(getMethod);
        String responseBody = new String(getMethod.getResponseBody());
        logger.info("result code: " + result);
        logger.info("method: " + getMethod.getName() + " at uri: " + base_uri + query);
        logger.info("response body: " + responseBody);
        assert result == HttpStatus.SC_OK : "Unexpected result: \n" + result;
    }


}
