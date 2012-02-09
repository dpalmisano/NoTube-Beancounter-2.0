package tv.notube.profiler.runner;

import tv.notube.profiler.DefaultProfilerFactory;
import tv.notube.profiler.Profiler;
import tv.notube.profiler.ProfilerException;
import org.apache.log4j.Logger;

/**
 * Main application's entry point. It implements a simple command
 * line interface.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Runner {

    private static Logger logger = Logger.getLogger(Runner.class);

    public static void main(String args[]) {
        Profiler profiler = DefaultProfilerFactory.getInstance().build();
        try {
            profiler.run();
        } catch (ProfilerException e) {
            logger.error("Error while profiling process", e);
            System.exit(-1);
        }
    }

}
