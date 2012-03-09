package tv.notube.crawler;

import java.util.UUID;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Crawler {

    public Report crawl() throws CrawlerException;

    public Report crawl(UUID userId) throws CrawlerException;

}
