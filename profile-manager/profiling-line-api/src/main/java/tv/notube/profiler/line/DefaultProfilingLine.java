package tv.notube.profiler.line;

import org.apache.log4j.Logger;

/**
 * Default implementation of {@link ProfilingLine}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultProfilingLine extends ProfilingLine {

    private static Logger logger = Logger.getLogger(DefaultProfilingLine.class);

    public DefaultProfilingLine(String name, String description) {
        super(name, description);
    }

    @Override
    public void run(ProfilingInput profilingInput) throws ProfilingLineException {
        try {
            logger.info("Profiling input: '" + profilingInput + "' started");
            super.profilingLineItem.execute(profilingInput.getValue());
        } catch (ProfilingLineItemException e) {
            final String errMsg = "Error while processing the Line Item: '" + super.name + "'";
            logger.error(errMsg, e);
            throw new ProfilingLineException(errMsg, e);
        }
    }
}

