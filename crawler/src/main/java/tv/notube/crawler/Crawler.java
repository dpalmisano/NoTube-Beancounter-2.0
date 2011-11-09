package tv.notube.crawler;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Crawler {

    public Report crawl() throws CrawlerException;

}
