package tv.notube.profiler.line;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * {@link ProfilingLine} main test class.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Test
public class ProfilingLineTest {

    private static Logger logger = Logger.getLogger(ProfilingLineTest.class);

    private ProfilingLine profilingLine;

    private ResultTestRepo repo;

    @BeforeTest
    public void setUp() throws ProfilingLineException, ProfilingLineItemException {
        profilingLine = new DefaultProfilingLine("test-line", "");
        ProfilingLineItem pli1 = new FakeProfilingLineItem1("count-item", "just counts the length of a string");
        ProfilingLineItem pli2 = new FakeProfilingLineItem2("producer-item", "just produces a string", 'a');
        repo = new ResultTestRepo();
        ProfilingLineItem pli3 = new LoggingProfilingLineItem("logging", "just logs", logger, repo);
        pli2.setNextProfilingLineItem(pli3);
        pli1.setNextProfilingLineItem(pli2);
        profilingLine.setProfilingLineItem(pli1);
    }

    @AfterTest
    public void tearDown() {
        profilingLine = null;
        repo = null;
    }

    @Test
    public void testProfilingLineRun() throws ProfilingLineException {
        final String testString = "the sky over Berlin";
        ProfilingInput profilingInput = new ProfilingInput(testString);
        profilingLine.run(profilingInput);
        String result = repo.getValue();
        Assert.assertNotNull(result);
        Assert.assertEquals(testString.length(), result.length());
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

        private ResultTestRepo repo;

        public LoggingProfilingLineItem(String name, String description, Logger logger, ResultTestRepo repo) {
            super(name, description);
            this.logger = logger;
            this.repo = repo;
        }

        public void execute(Object input) throws ProfilingLineItemException {
            logger.info(input);
            repo.setValue((String) input);
        }
    }

    public static class ResultTestRepo {

        private String value;

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }
}
