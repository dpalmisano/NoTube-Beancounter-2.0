package tv.notube.extension.profilingline;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.profiler.DefaultProfilerFactory;
import tv.notube.profiler.Profiler;
import tv.notube.profiler.ProfilerException;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TestWithRunner {

    private Profiler profiler;

    @BeforeTest
    public void setUp() {
        profiler = DefaultProfilerFactory.getInstance().build();
    }

    @Test
    public void test() throws ProfilerException {
        profiler.run();
    }

}
