package tv.notube.extension.profilingline;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.profiler.DefaultProfilerFactory;
import tv.notube.profiler.Profiler;
import tv.notube.profiler.ProfilerException;
import tv.notube.profiler.configuration.ConfigurationManager;
import tv.notube.profiler.configuration.ProfilerConfiguration;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TestWithRunner {

    private static String FILEPATH = "src/test/resources/configuration.xml";

    private ProfilerConfiguration configuration;

    @BeforeTest
    public void setUp() {
        configuration = ConfigurationManager
                .getInstance(FILEPATH)
                .getConfiguration();
    }

    @Test
    public void test() throws ProfilerException {
        Profiler p = DefaultProfilerFactory.getInstance(configuration).build();
        p.run();
    }

}
