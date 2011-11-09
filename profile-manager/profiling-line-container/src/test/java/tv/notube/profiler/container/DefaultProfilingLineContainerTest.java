package tv.notube.profiler.container;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.profiler.line.DefaultProfilingLine;
import tv.notube.profiler.line.ProfilingInput;
import tv.notube.profiler.line.ProfilingLine;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;
import tv.notube.profiler.line.ProfilingResult;

/**
 * Reference test class for {@link DefaultProfilingLineContainer}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultProfilingLineContainerTest {

    private static Logger logger = Logger.getLogger(DefaultProfilingLineContainerTest.class);

    private ProfilingLineContainer container;

    private final String lineName = "test-pline";  

    @BeforeTest
    public void setUp() throws ProfilingLineContainerException {
        container = new DefaultProfilingLineContainer();
        Assert.assertEquals(container.getNumberOfProfilingLines(), 0);
        Assert.assertEquals(container.getProfilingLineNames().size(), 0);
        ProfilingLine profilingLine = new DefaultProfilingLine(lineName, "just for test");
        ProfilingLineItem pli1 = new FakeProfilingLineItem1("count-item", "just counts the length of a string");
        ProfilingLineItem pli2 = new FakeProfilingLineItem2("writer-item", "just produces a string", 'a');
        ProfilingLineItem pli3 = new LoggingProfilingLineItem("logger-item", "just logs", logger);
        pli2.setNextProfilingLineItem(pli3);
        pli1.setNextProfilingLineItem(pli2);
        profilingLine.setProfilingLineItem(pli1);

        container.addProfilingLine(profilingLine);
        Assert.assertEquals(container.getNumberOfProfilingLines(), 1);
        Assert.assertEquals(container.getProfilingLineNames().size(), 1);
        Assert.assertTrue(container.getProfilingLineNames().contains(lineName));
    }

    @AfterTest
    public void tearDown() {
        container = null;
    }

    @Test
    public void testProfile() throws ProfilingLineContainerException {
        final String testString = "the sky over Berlin";
        ProfilingInput profilingInput = new ProfilingInput(testString);
        ProfilingResult result = container.profile(profilingInput, lineName);
        Assert.assertNotNull(result);
        logger.info(result.getValue());
    }

    public static class FakeProfilingLineItem1 extends ProfilingLineItem {

        public FakeProfilingLineItem1(String name, String description) {
            super(name, description);
        }

        public void execute(Object input) throws ProfilingLineItemException {
            String castedInput = (String) input;
            Integer rawResult = new Integer(castedInput.length());
            super.next.execute(rawResult);
        }
    }

    public static class FakeProfilingLineItem2 extends ProfilingLineItem {

            private char charToBeWritten;

            public FakeProfilingLineItem2(String name, String description, char charToBeWritten) {
                super(name, description);
                this.charToBeWritten = charToBeWritten;
            }

            public void execute(Object input) throws ProfilingLineItemException {
                Integer integer = (Integer) input;
                String rawResult = "";
                for(int i=0; i<integer.intValue(); i++) {
                     rawResult += this.charToBeWritten;
                }
                super.next.execute(rawResult);
            }
        }

    public static class LoggingProfilingLineItem extends ProfilingLineItem {

        private Logger logger;

        public LoggingProfilingLineItem(String name, String description, Logger logger) {
            super(name, description);
            this.logger = logger;
        }

        public void execute(Object input) throws ProfilingLineItemException {
            logger.info(input);
            super.next.execute(input);
        }
    }

}
