package tv.notube.crawler;

import org.apache.log4j.Logger;
import tv.notube.crawler.runnable.Spider;
import tv.notube.crawler.runnable.SpiderException;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ParallelCrawlerImpl extends AbstractCrawler {

    private static Logger logger = Logger.getLogger(ParallelCrawlerImpl.class);

    private ExecutorService executor = Executors.newCachedThreadPool();

    public ParallelCrawlerImpl(UserManager userManager) {
        super(userManager);
    }

    public Report crawl() throws CrawlerException {
        long start = System.currentTimeMillis();
        List<UUID> ids;
        try {
            ids = getUserManager().getUsersToCrawled();
        } catch (UserManagerException e) {
            final String errMsg = "Error while retrieving users to be crawled";
            logger.error(errMsg, e);
            throw new CrawlerException(errMsg, e);
        }
        int count = 0;
        for(UUID id : ids) {
            if(!executor.isShutdown()) {
                count++;
                try {
                    executor.submit(new Spider(
                            "spider-" + id.toString(),
                            getUserManager(),
                            id)
                    );
                } catch (SpiderException e) {
                    final String errMsg = "Error while instantiating Job '" + id.toString() + "'";
                    logger.error(errMsg, e);
                    throw new CrawlerException(errMsg ,e);
                }
            }
        }
        long end = System.currentTimeMillis();
        return new Report(count, start, end);
    }
}
