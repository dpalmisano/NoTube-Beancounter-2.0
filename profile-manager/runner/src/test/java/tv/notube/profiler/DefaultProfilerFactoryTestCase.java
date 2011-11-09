package tv.notube.profiler;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.profiler.configuration.ConfigurationManager;
import tv.notube.profiler.configuration.ProfilerConfiguration;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultProfilerFactoryTestCase {

    private static String FILEPATH = "src/test/resources/configuration.xml";

    private ProfilerConfiguration configuration;

    @BeforeTest
    public void setUp() {
        configuration = ConfigurationManager
                .getInstance(FILEPATH)
                .getConfiguration();
    }

    @Test
    public void test() {
        Profiler p = DefaultProfilerFactory.getInstance(configuration).build();
        Assert.assertNotNull(p);
    }

}
