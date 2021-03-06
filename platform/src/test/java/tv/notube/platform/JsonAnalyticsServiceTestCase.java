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
public class JsonAnalyticsServiceTestCase extends AbstractJerseyTestCase {

    public JsonAnalyticsServiceTestCase() {
        super(9995);
    }

    @Test
    public void getAnalysisResult() throws IOException {
        final String baseQuery = "analytics/analysis/%s/%s/%s?param=%s";
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

    @Test
    public void getAnalysisResultWithNoParameters() throws IOException {
        final String baseQuery = "analytics/analysis/%s/%s/%s";
        final String name = "activity-analysis";
        final String user = "8c33b0e6-d3cf-4909-b04c-df93056e64a8";
        final String methodName = "getTotalActivities";

        final String query = String.format(
                baseQuery,
                name,
                user,
                methodName
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
