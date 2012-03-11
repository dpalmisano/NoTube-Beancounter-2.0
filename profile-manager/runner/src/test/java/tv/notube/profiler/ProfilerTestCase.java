package tv.notube.profiler;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProfilerTestCase {

    private Profiler profiler;

    @BeforeTest
    public void setUp() {
        profiler = DefaultProfilerFactory.getInstance().build();
    }

    @Test
    public void test() throws ProfilerException {
        profiler.run(UUID.fromString("85a9241a-53d2-4512-bac5-11badcfa6c4c"));
    }



}
