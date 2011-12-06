package tv.notube.platform;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import tv.notube.platform.jersey.JerseyRESTFrontendConfig;
import tv.notube.platform.jersey.JerseyRESTFrontendFactory;
import tv.notube.platform.jersey.RESTFrontend;
import tv.notube.platform.jersey.RESTFrontendConfig;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class AbstractJerseyTestCase {

    protected static final Logger logger = Logger.getLogger(AbstractJerseyTestCase.class);

    protected static final String root_dir = "";

    private static final String base_uri_str = "http://localhost:%d/";

    protected final URI base_uri;

    protected Gson gson;

    private RESTFrontend frontend;

    protected AbstractJerseyTestCase(int port) {
        try {
            base_uri = new URI(String.format(base_uri_str, port));
        } catch (URISyntaxException urise) {
            throw new RuntimeException(urise);
        }
    }

    @BeforeClass
    public void setUp() throws Exception {
        startFrontendService();
    }

    protected void startFrontendService() {
        RESTFrontendConfig config = new JerseyRESTFrontendConfig(base_uri);
        JerseyRESTFrontendFactory factory = JerseyRESTFrontendFactory.getInstance();

        frontend = factory.create(config);
        logger.info("Starting Grizzly...");
        frontend.startService();
        logger.info(
                String.format("Grizzly started at location %s.\n", base_uri)
        );
    }

    @AfterClass
    public void tearDown() throws InterruptedException {
        frontend.stopService();
        logger.info("Grizzly stopped");
        this.gson = null;
    }
}
