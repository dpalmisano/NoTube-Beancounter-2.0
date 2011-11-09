package tv.notube.profiler.runner;

import tv.notube.profiler.DefaultProfilerFactory;
import tv.notube.profiler.Profiler;
import tv.notube.profiler.ProfilerException;
import tv.notube.profiler.configuration.ConfigurationManager;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import tv.notube.profiler.configuration.ProfilerConfiguration;
import tv.notube.profiler.container.ProfilingLineContainer;
import tv.notube.profiler.data.DataManager;
import tv.notube.profiler.storage.ProfileStore;

/**
 * Main application's entry point. It implement a simple command
 * line interface.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Runner {

    private static Logger logger = Logger.getLogger(Runner.class);

    public static void main(String args[]) {

        final String CONFIGURATION = "configuration";

        Options options = new Options();
        options.addOption(CONFIGURATION, true, "XML Configuration file.");
        CommandLineParser commandLineParser = new PosixParser();
        CommandLine commandLine = null;
        if(args.length != 2) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Runner", options);
            System.exit(-1);
        }
        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            logger.error("Error while parsing arguments", e);
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Runner", options);
            System.exit(-1);
        }
        String confFilePath = commandLine.getOptionValue(CONFIGURATION);

        /**
         * Parse the configuration file and instantiates all the needed dependencies
         */
        logger.info("Loading configuration from: '" + confFilePath + "'");
        ConfigurationManager configurationManager =
                ConfigurationManager.getInstance(confFilePath);
        ProfilerConfiguration configuration = configurationManager.getConfiguration();
        Profiler profiler = DefaultProfilerFactory.getInstance(configuration).build();

        try {
            profiler.run();
        } catch (ProfilerException e) {
            logger.error("Error while profiling process", e);
            System.exit(-1);
        }
    }

}
