package tv.notube.crawler;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import tv.notube.crawler.configuration.ConfigurationManager;
import tv.notube.crawler.configuration.CrawlerConfiguration;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Runner {

    private static Logger logger = Logger.getLogger(Runner.class);

    private static Crawler crawler;

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("configuration", true, "Crawler XML configuration file");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException("Error while parsing the command line", e);
        }
        if (!cmd.hasOption("configuration") || cmd.getOptionValue("configuration") == null) {
            logger.fatal("-configuration parameter is missing");
            System.exit(-1);
        }
        String filePath = cmd.getOptionValue("configuration");
        CrawlerConfiguration crawlerConfiguration = ConfigurationManager
                .getInstance(filePath)
                .getCrawlerConfiguration();
        try {
            crawler = DefaultCrawlerFactory.getInstance(crawlerConfiguration).build();
        } catch (CrawlerFactoryException e) {
            logger.fatal("Something went wrong while instantiating the crawler", e);
            System.exit(-1);
        }
        logger.info("Crawl started at: " + new DateTime(System.currentTimeMillis()));
        Report report;
        try {
            report = crawler.crawl();
        } catch (CrawlerException e) {
            logger.fatal("Something went wrong while instantiating the crawler", e);
            System.exit(-1);
            throw new RuntimeException();
        }
        logger.info("Crawl started at: " + new DateTime(System.currentTimeMillis()));
        logger.info(report.getSubmittedProcesses()
                + " processes have been submitted in "
                + (report.getEndedAt() - report.getStartedAt()) / 1000
                + " seconds");
    }

}
