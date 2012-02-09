package tv.notube.profiler;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultProfilerFactoryTestCase {

    private Profiler profiler;

    @BeforeTest
    public void setUp() {
        profiler = DefaultProfilerFactory.getInstance().build();
    }

    @Test
    public void test() {
        Assert.assertNotNull(profiler);
    }

}
